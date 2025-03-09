package com.hsbc.fraud.application.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

import com.hsbc.fraud.application.bo.AntiFraudInput;
import com.hsbc.fraud.application.bo.TransactionBehavior;
import com.hsbc.fraud.application.bo.UserIdentity;
import com.hsbc.fraud.application.constants.CustomerTypeEnum;
import com.hsbc.fraud.application.constants.OperationTypeEnum;
import com.hsbc.fraud.application.event.consumer.EventData;
import com.hsbc.fraud.infrastructure.dao.ActionTypeEnum;
import com.hsbc.fraud.infrastructure.dao.AlertEvent;
import com.hsbc.fraud.infrastructure.dao.AntiFraudNotify;
import com.yomahub.liteflow.flow.LiteflowResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;

import com.yomahub.liteflow.core.FlowExecutor;
import com.yomahub.liteflow.flow.FlowBus;

@SpringBootTest
public class AntiFraudProcessorTest {

    @Autowired
    private AntiFraudProcessor antiFraudProcessor;

    @MockBean
    private DataLoader dataLoader;

    @SpyBean
    private AntiFraudNotify antiFraudNotify;

    @BeforeEach
    void setup() {
        // 确保LiteFlow规则链初始化
        FlowBus.cleanCache();
    }

    @Test
    void whenAmountExceedsThreshold_ShouldTriggerReject() throws Exception {
        // 构造测试数据：金额15000，账户003（不触发可疑账户检查）
        EventData eventData = createEventData("15000", "003");

        // 配置DataLoader返回模拟数据
        AntiFraudInput mockInput = createAntiFraudInput(eventData);
        when(dataLoader.loadData(anyString())).thenReturn(mockInput);

        // 执行反欺诈处理流程
        antiFraudProcessor.process(eventData);

        // 验证风控触发并检查动作类型
        ArgumentCaptor<AlertEvent> captor = ArgumentCaptor.forClass(AlertEvent.class);
        verify(antiFraudNotify).notifyRisks(captor.capture());
        assertEquals(ActionTypeEnum.REJECT, captor.getValue().getActionType());
    }

    @Test
    void whenAccountIsSuspicious_ShouldTriggerReject() throws Exception {
        // 构造测试数据：金额5000，账户001（触发可疑账户检查）
        EventData eventData = createEventData("5000", "001");

        AntiFraudInput mockInput = createAntiFraudInput(eventData);
        when(dataLoader.loadData(anyString())).thenReturn(mockInput);

        antiFraudProcessor.process(eventData);

        ArgumentCaptor<AlertEvent> captor = ArgumentCaptor.forClass(AlertEvent.class);
        verify(antiFraudNotify).notifyRisks(captor.capture());
        assertEquals(ActionTypeEnum.REJECT, captor.getValue().getActionType());
    }

    @Test
    void whenNoConditionMet_ShouldNotTriggerReject() throws Exception {
        // 构造测试数据：金额5000，账户003（不触发任何规则）
        EventData eventData = createEventData("5000", "003");

        AntiFraudInput mockInput = createAntiFraudInput(eventData);
        when(dataLoader.loadData(anyString())).thenReturn(mockInput);

        antiFraudProcessor.process(eventData);

        // 验证未调用风控通知
        verify(antiFraudNotify, never()).notifyRisks(any());
    }

    private EventData createEventData(String amount, String accountId) {
        EventData eventData = new EventData();
        eventData.setTimestamp(ZonedDateTime.now());
        eventData.setCustomerType(CustomerTypeEnum.PERSONAL);
        eventData.setOperationType(OperationTypeEnum.CROSS_BORDER_TRANSFER);
        eventData.setCustomerId("CUST123");
        eventData.setAccountId(accountId);
        eventData.setAmount(new BigDecimal(amount));
        return eventData;
    }

    private AntiFraudInput createAntiFraudInput(EventData eventData) {
        AntiFraudInput input = new AntiFraudInput();
        UserIdentity identity = new UserIdentity();
        identity.setCustomerId(eventData.getCustomerId());
        input.setIdentity(identity);

        TransactionBehavior transaction = new TransactionBehavior();
        transaction.setAccountId(eventData.getAccountId());
        transaction.setAmount(eventData.getAmount());
        input.setTransaction(transaction);

        return input;
    }
}
