package com.hsbc.fraud.application.bo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.Size;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DeviceProfile {
    @Size(min = 32, max = 32, message = "设备指纹长度异常")
    private String deviceFingerprint;

    private Boolean emulatorDetected;

    @DecimalMax("1.0")
    private Double gpsSpoofingRisk;
}

