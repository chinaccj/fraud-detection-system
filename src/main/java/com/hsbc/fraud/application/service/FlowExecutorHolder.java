package com.hsbc.fraud.application.service;


import com.hsbc.fraud.application.constants.CustomerTypeEnum;
import com.hsbc.fraud.application.constants.OperationTypeEnum;
import com.yomahub.liteflow.core.FlowExecutor;
import com.yomahub.liteflow.property.LiteflowConfig;

import java.util.HashMap;
import java.util.Map;

public class FlowExecutorHolder {

    private static volatile FlowExecutor instance;

    static Map<String,FlowExecutor> map = new HashMap<>();


    public static FlowExecutor getInstance(CustomerTypeEnum customerType, OperationTypeEnum operationTypeEnum) {
        String key = customerType.getCode()+"_"+operationTypeEnum.getCode();
        FlowExecutor flowExecutor = map.get(key);
        if (flowExecutor == null) {
            synchronized (FlowExecutorHolder.class) {
                if (flowExecutor == null) {

                    // For the sake of simplicity, a rule chain is hard-coded.
                    // According to the production business, different business
                    // scenarios (customer types + transaction types) require the initialization of different rule chains.

                    LiteflowConfig config = new LiteflowConfig();

                    config.setRuleSource("riskControl.el.xml");

                    flowExecutor = new FlowExecutor();
                    flowExecutor.setLiteflowConfig(config);
                    flowExecutor.init(true);
                }
            }
        }
        return flowExecutor;
    }
}