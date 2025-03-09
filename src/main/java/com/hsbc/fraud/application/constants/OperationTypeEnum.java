package com.hsbc.fraud.application.constants;

public enum OperationTypeEnum {
    PAYMENT("PAY", "Payment"),
    CROSS_BORDER_TRANSFER("CBT", "Cross-border Transfer"),
    PRIVATE_BANKING_INVESTMENT("PBI", "Private Banking Investment"),
    E_CREDIT_APPLICATION("ECA", "E-Credit Application");

    private final String code;
    private final String englishName;

    OperationTypeEnum(String code, String englishName) {
        this.code = code;
        this.englishName = englishName;
    }

    public String getCode() {
        return code;
    }

    public String getEnglishName() {
        return englishName;
    }

    // 根据编码解析枚举
    public static OperationTypeEnum fromCode(String code) {
        for (OperationTypeEnum type : values()) {
            if (type.code.equalsIgnoreCase(code)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Invalid operation code: " + code);
    }

    // 根据中文名称解析枚举
    public static OperationTypeEnum fromChineseName(String chineseName) {
        switch (chineseName) {
            case "支付": return PAYMENT;
            case "跨境转账": return CROSS_BORDER_TRANSFER;
            case "私人银行投资": return PRIVATE_BANKING_INVESTMENT;
            case "电子信贷申请": return E_CREDIT_APPLICATION;
            default: throw new IllegalArgumentException("未知操作类型: " + chineseName);
        }
    }
}

