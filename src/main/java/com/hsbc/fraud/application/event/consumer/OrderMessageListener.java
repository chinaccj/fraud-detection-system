package com.hsbc.fraud.application.event.consumer;

import com.hsbc.fraud.application.service.AntiFraudProcessor;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.annotation.SelectorType;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@RocketMQMessageListener(
        topic = "BIZ_EVENT_TOPIC",
        consumerGroup = "order-consumer-group",
        selectorType = SelectorType.TAG,
        selectorExpression = "PAY_SUCCESS"
)
public class OrderMessageListener implements RocketMQListener<EventData> {
    @Autowired
    AntiFraudProcessor antiFraudProcessor;
    @Override
    public void onMessage(EventData message) {
        // 处理消息逻辑

        antiFraudProcessor.process(message);

    }


}
