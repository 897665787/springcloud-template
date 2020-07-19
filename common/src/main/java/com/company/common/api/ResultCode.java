package com.company.common.api;

public enum ResultCode {
	SUCCESS(0, "成功"), FAIL(99, "失败"),
	/* 自定义结果码 */
	SYSTEM_ERROR(1001, "系统错误"), PARAM_INVALID(1002, "参数无效");

	private Integer code;
	private String message;

	ResultCode(Integer code, String message) {
		this.code = code;
		this.message = message;
	}

	public Integer getCode() {
		return code;
	}

	public String getMessage() {
		return message;
	}
}
