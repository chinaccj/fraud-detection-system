package com.hsbc.fraud.application.constants;

public enum CustomerTypeEnum {
    PERSONAL("PERSONAL", "Individual Customer"),
    ENTERPRISE("ENTERPRISE", "Corporate Client"),
    HIGH_NET_WORTH("HNW", "High Net Worth Client"),
    SMALL_MICRO_BUSINESS("SMB", "Small/Micro Business");

    private final String code;
    private final String englishName;

    CustomerTypeEnum(String code, String englishName) {
        this.code = code;
        this.englishName = englishName;
    }

    public String getCode() {
        return code;
    }

    public String getEnglishName() {
        return englishName;
    }

    // 根据code解析枚举
    public static CustomerTypeEnum fromCode(String code) {
        for (CustomerTypeEnum type : values()) {
            if (type.code.equalsIgnoreCase(code)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Invalid customer type code: " + code);
    }

    // 兼容中文名称的解析（按业务需要可选）
    public static CustomerTypeEnum fromChineseName(String chineseName) {
        switch (chineseName) {
            case "个人客户": return PERSONAL;
            case "企业客户": return ENTERPRISE;
            case "高净值客户": return HIGH_NET_WORTH;
            case "小微商户": return SMALL_MICRO_BUSINESS;
            default: throw new IllegalArgumentException("未知客户类型: " + chineseName);
        }
    }
}