package com.company.user.api.enums;

public enum UserStatus {
	RIGHTS(1, "正常"), BUY_MEMBER(2, "冻结");
	private Integer code;
	private String desc;

	UserStatus(Integer code, String desc) {
		this.code = code;
		this.desc = desc;
	}

	public Integer getCode() {
		return code;
	}

	public String getDesc() {
		return desc;
	}
}
