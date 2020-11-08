package com.company.order.api.enums;

public enum OrderType {
	RIGHTS(1, "权益"), BUY_MEMBER(2, "购买会员");
	private Integer type;
	private String desc;

	OrderType(Integer type, String desc) {
		this.type = type;
		this.desc = desc;
	}

	public Integer getType() {
		return type;
	}

	public String getDesc() {
		return desc;
	}
}
