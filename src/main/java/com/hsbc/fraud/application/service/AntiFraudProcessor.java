package com.hsbc.fraud.application.service;

import com.hsbc.fraud.application.bo.AntiFraudInput;
import com.hsbc.fraud.application.constants.CustomerTypeEnum;
import com.hsbc.fraud.application.constants.OperationTypeEnum;
import com.hsbc.fraud.application.event.consumer.EventData;
import com.yomahub.liteflow.core.FlowExecutor;
import com.yomahub.liteflow.flow.LiteflowResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AntiFraudProcessor {
    private static final Logger logger = LoggerFactory.getLogger(AntiFraudProcessor.class);

    @Autowired
    private DataLoader dataLoader;

    public void process(EventData message) {

        //Load customer - related data as the input parameters for the
        // anti - fraud model. In a production environment,
        // the anti - fraud model may have tens of thousands, or even
        // hundreds of thousands, millions of parameters.
        // parameters include  device data, historical transaction data, third - party data, and so on.
        // dataLoader

        AntiFraudInput antiFraudInput = dataLoader.loadData(message.getCustomerId());
        antiFraudInput.getIdentity().setCustomerId(message.getCustomerId());
        antiFraudInput.getTransaction().setAccountId(message.getAccountId());
        antiFraudInput.getTransaction().setAmount(message.getAmount());

        // Different customer types or transaction types use different rule chains for anti - fraud processing.
        // for example ,Credit card transactions and large-value transfers may require different feature engineering and models.

        // PERSONAL Customer type and CROSS_BORDER_TRANSFER use such rule trains to do fraud
        if (CustomerTypeEnum.PERSONAL.equals(message.getCustomerType()) &&
                OperationTypeEnum.CROSS_BORDER_TRANSFER.equals(message.getOperationType())) {
            FlowExecutor flowExecutor = FlowExecutorHolder.getInstance(message.getCustomerType(), message.getOperationType());
            LiteflowResponse response = flowExecutor.execute2Resp(
                    "personal_cross_border_transfer",
                    null,
                    antiFraudInput);

            if (response.isSuccess()) {
                logger.info("[风控触发] 金额:%.2f 可疑账户:%s%n",
                        antiFraudInput.getTransaction().getAmount(),
                        antiFraudInput.getTransaction().getAccountId());
            } else {
                logger.info("[正常交易] 未触发风控规则");
            }
        }
        else{

            //"To simplify operations, also use the 'personal account + cross-border transfer' rule chain."

            FlowExecutor flowExecutor = FlowExecutorHolder.getInstance(message.getCustomerType(), message.getOperationType());
            LiteflowResponse response = flowExecutor.execute2Resp(
                    "personal_cross_border_transfer",
                    null,
                    antiFraudInput);

            if (response.isSuccess()) {
                logger.info("[风控触发] 金额:%.2f 可疑账户:%s%n",
                        antiFraudInput.getTransaction().getAmount(),
                        antiFraudInput.getTransaction().getAccountId());
            } else {
                logger.info("[正常交易] 未触发风控规则");
            }
        }

    }

}