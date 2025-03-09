package com.hsbc.fraud.application.bo;

import com.hsbc.fraud.application.constants.OperationTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

// 交易行为特征层
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TransactionBehavior {
    @Min(value = 0, message = "交易次数不能为负")
    private Integer lastHourTransactions;

    @DecimalMin("0.0")
    private Double avgTransactionAmount;

    @NotNull
    private Boolean crossBorderFlag;


    private OperationTypeEnum bizType;
    private BigDecimal amount;
    private String currency;
    private String accountId;
}
