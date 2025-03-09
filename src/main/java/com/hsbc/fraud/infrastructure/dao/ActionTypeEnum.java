package com.hsbc.fraud.infrastructure.dao;

public enum ActionTypeEnum {

    MANUALLY_REVIEW("MANUALLY_REVIEW", "Manually review"),
    REJECT("REJECT", "reject");

    private final String code;
    private final String name;

    ActionTypeEnum(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    // 根据 code 获取枚举实例
    public static ActionTypeEnum getByCode(String code) {
        for (ActionTypeEnum type : ActionTypeEnum.values()) {
            if (type.getCode().equals(code)) {
                return type;
            }
        }
        return null;
    }
}