package com.hsbc.fraud.infrastructure.dao;

import org.apache.rocketmq.client.producer.SendResult;

/**
 * 消息发送异常（包含重试上下文）
 */
public class MessageSendException extends RuntimeException {
    private  String topic;
    private  String messageKey = null;
    private  SendResult partialResult = null;


    public MessageSendException(String message
                                ,String topic,String messageKey
    ) {
        super(message);
        this.topic = topic;
        this.messageKey = messageKey;
    }


    // 基础构造器
    public MessageSendException(String message, Throwable cause,
                                String topic, String messageKey
                                ) {
        super(message, cause);
        this.topic = topic;
        this.messageKey = messageKey;
        this.partialResult = null;
    }

    // 包含部分结果的构造器
    public MessageSendException(String message, SendResult partialResult,
                                String topic, String messageKey) {
        super(message);
        this.topic = topic;
        this.messageKey = messageKey;
        this.partialResult = partialResult;
    }

    // Getters
    public String getTopic() { return topic; }
    public String getMessageKey() { return messageKey; }
    public SendResult getPartialResult() { return partialResult; }

    // 生成详细错误信息
    @Override
    public String getMessage() {
        return String.format("[MessageSendFailed] topic=%s, key=%s, %s",
                topic, messageKey, super.getMessage());
    }
}