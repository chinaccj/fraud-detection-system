package com.hsbc.fraud.infrastructure.dao;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.apache.rocketmq.client.producer.SendResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AntiFraudNofiyImpl implements AntiFraudNotify{
    private static final Logger logger = LoggerFactory.getLogger(AntiFraudNotify.class);

    @Autowired
    private EnhancedMQProducer enhancedMQProducer;

    @Override
    public void notifyRisks(AlertEvent event) {
        String contentWithJson = JSON.toJSONString(event,
                SerializerFeature.PrettyFormat,  // 美化输出
                SerializerFeature.WriteDateUseDateFormat);

        SendResult sendResult = enhancedMQProducer.syncSendWithRetry("topic", contentWithJson);
        logger.info("notifyRisks"+event.toString());
    }
}
