package com.company.order.api.enums;

/**
 * 退款发起类型
 */
public enum RefundTypeEnum {

    USER("USER", "用户发起"),

    ADMIN("ADMIN", "客服发起"),

    SUPPLIER("SUPPLIER", "供应商发起"),

    SYSTEM_PURCHASE_FAIL("SYSTEM_PURCHASE_FAIL", "系统发起-发货失败"),

    SYSTEM_AUTO_REFUND("SYSTEM_AUTO_REFUND", "系统发起-自动退款"),

    SYSTEM_HELP_BUY_TIMEOUT("SYSTEM_HELP_BUY_TIMEOUT", "系统发起-助力超时"),

    GROUP_MEAL_MERCHANT_REFUND("GROUP_MEAL_MERCHANT_REFUND", "团餐-商家端发起"),

    GROUP_MEAL_USER_REFUND("GROUP_MEAL_USER_REFUND", "团餐-用户端发起"),
    ;

    private String code;

    private String desc;

    RefundTypeEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
    
    public static RefundTypeEnum of(String code) {
        for (RefundTypeEnum item : RefundTypeEnum.values()) {
            if (item.getCode().equals(code)) {
                return item;
            }
        }
        return USER;
    }
}
