package com.hsbc.fraud.infrastructure.dao;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class AlertEvent {
    @NotBlank(message = "用户ID不能为空")
    private String customerId;

    @NotBlank(message = "账户ID不能为空")
    private String accountId;

    // Manually review and reject.

    @NotBlank(message = "actionType不能为空")
    private ActionTypeEnum actionType;

    @NotBlank(message = "content不能为空")
    private String content;



}
