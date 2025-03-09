package com.hsbc.fraud.application.manager;

import com.hsbc.fraud.application.bo.AntiFraudInput;
import com.hsbc.fraud.infrastructure.dao.ActionTypeEnum;
import com.hsbc.fraud.infrastructure.dao.AlertEvent;
import com.hsbc.fraud.infrastructure.dao.AntiFraudNotify;
import com.yomahub.liteflow.annotation.LiteflowComponent;
import com.yomahub.liteflow.core.NodeComponent;
import org.springframework.beans.factory.annotation.Autowired;


@LiteflowComponent(id = "riskAction")
public class RiskActionComponent extends NodeComponent {
    @Autowired
    AntiFraudNotify antiFraudNotify;

    @Override
    public void process() {
        // 这里添加具体风控逻辑

        boolean amountCheck = this.getSlot().getIfResult("amountCheck");
        boolean suspAccountCheck = this.getSlot().getIfResult("suspAccountCheck");
        if(amountCheck || suspAccountCheck) {

            AntiFraudInput context = this.getFirstContextBean();

            AlertEvent event = new AlertEvent();
            event.setCustomerId(context.getIdentity().getCustomerId());
            event.setAccountId(context.getTransaction().getAccountId());
            event.setActionType(ActionTypeEnum.REJECT);
            event.setContent("reject reason: xxxxx");
            antiFraudNotify.notifyRisks(event);
        }
    }
}
