package com.hsbc.fraud.infrastructure.dao;

import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.protocol.ResponseCode;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.apache.rocketmq.spring.support.RocketMQHeaders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class EnhancedMQProducer {
    private static final Logger logger = LoggerFactory.getLogger(EnhancedMQProducer.class);

    // 最大应用层重试次数（配合RocketMQ内置重试使用）
    private static final int MAX_RETRIES = 2;
    private final RocketMQTemplate rocketMQTemplate;

    public EnhancedMQProducer(RocketMQTemplate rocketMQTemplate) {
        this.rocketMQTemplate = rocketMQTemplate;
    }

    public SendResult syncSendWithRetry(String topic, String messageBody) throws MessageSendException {
        String clientMsgId = "CLIENT_" + UUID.randomUUID();

        int retryCount = 0;
        String msgId = "MSG_" + UUID.randomUUID();
        Message<String> message = MessageBuilder.withPayload(messageBody)
                .setHeader(RocketMQHeaders.KEYS, msgId)
                .build();

        while (retryCount <= MAX_RETRIES) {
            try {
                SendResult sendResult = rocketMQTemplate.syncSend(topic, message);
                logger.info("消息发送成功! Topic: {}, MsgId: {}", topic, sendResult.getMsgId());
                return sendResult;
            } catch (Exception e) {

                retryCount++;
                if (shouldRetry(e) && retryCount <= MAX_RETRIES) {
                    logger.warn("消息发送失败，进行第{}次重试. Topic: {}", retryCount, topic, e);
                    waitForRetry(retryCount);
                } else {
                    handleFinalFailure(message, e);
                    throw new MessageSendException("消息发送最终失败", e,topic,msgId);
                }
            }
        }

        throw new MessageSendException("消息发送最终失败",topic,msgId);
    }

    private boolean shouldRetry(Exception e) {
        // 根据异常类型判断是否可重试
        return (e instanceof RemotingException) ||
                (e instanceof MQClientException) ||
                (e instanceof MQBrokerException && ((MQBrokerException)e).getResponseCode() == ResponseCode.TOPIC_NOT_EXIST);
    }

    private void waitForRetry(int retryCount) {
        try {
            // 指数退避策略
            long sleepTime = (long) (Math.pow(2, retryCount) * 500);
            Thread.sleep(sleepTime);
        } catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
        }
    }

    private void handleFinalFailure(Message<?> message, Exception e) {
        // 记录失败详情到数据库
        String msgKey = message.getHeaders().get(RocketMQHeaders.KEYS, String.class);
        logger.error("消息最终发送失败! Key: {}, Body: {}", msgKey, message.getPayload(), e);

        // 推送到死信队列（需实现）
        // deadLetterQueue.push(message);
    }
}
