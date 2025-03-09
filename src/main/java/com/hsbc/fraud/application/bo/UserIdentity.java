package com.hsbc.fraud.application.bo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserIdentity {
    @NotBlank(message = "用户ID不能为空")
    private String customerId;

    @Pattern(regexp = "\\d{4}-\\d{2}-\\d{2}", message = "注册日期格式错误")
    private String registerDate;

    @AssertTrue(message = "需完成实名认证")
    private Boolean identityVerified;




}
