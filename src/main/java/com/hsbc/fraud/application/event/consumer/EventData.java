package com.hsbc.fraud.application.event.consumer;

import com.hsbc.fraud.application.constants.CustomerTypeEnum;
import com.hsbc.fraud.application.constants.OperationTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class EventData {
    private ZonedDateTime timestamp;

    /**
     * 业务类型
     */
    private String eventType;

    private CustomerTypeEnum customerType;

    private String customerId;

    private String accountId;

    private OperationTypeEnum operationType;
    private BigDecimal amount;
    private String currency;
    private String targetAccountId;


}

