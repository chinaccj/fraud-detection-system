package com.hsbc.fraud.application.bo;
import com.yomahub.liteflow.slot.DefaultContext;
import lombok.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Map;

@Data
@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AntiFraudInput extends DefaultContext {
    @NotNull
    @Valid  // 启用嵌套验证
    private UserIdentity identity;

    @Valid
    private TransactionBehavior transaction;

    @Valid
    private DeviceProfile device;

    private Map<String, Double> thirdPartyScores;
}
