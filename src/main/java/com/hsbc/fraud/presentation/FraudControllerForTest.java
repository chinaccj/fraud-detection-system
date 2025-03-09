package com.hsbc.fraud.presentation;


import com.hsbc.fraud.application.constants.CustomerTypeEnum;
import com.hsbc.fraud.application.constants.OperationTypeEnum;
import com.hsbc.fraud.application.event.consumer.EventData;
import com.hsbc.fraud.application.service.AntiFraudProcessor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.math.BigDecimal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/fraud")
public class FraudControllerForTest {
    @Autowired
    AntiFraudProcessor antiFraudProcessor;
    @PostMapping("/v1/trade")
    Object queryDatabaseOperationLog(@Valid @RequestParam String customerId
            , @RequestParam String accountId,@RequestParam BigDecimal amount) {

        EventData eventData = new EventData();
        eventData.setAmount(amount);
        eventData.setAccountId(accountId);
        eventData.setCustomerId(customerId);
        eventData.setCustomerType(CustomerTypeEnum.PERSONAL);
        eventData.setOperationType(OperationTypeEnum.CROSS_BORDER_TRANSFER);

        antiFraudProcessor.process(eventData);

        return null;
    }

}
