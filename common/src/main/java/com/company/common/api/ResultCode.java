package com.company.common.api;

import com.company.common.exception.BusinessException;

public enum ResultCode {
	SUCCESS(0, "成功"), FAIL(99, "失败"), FALLBACK(104, "API熔断"),
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
	
	public static ResultCode of(Integer code) {
		for (ResultCode item : ResultCode.values()) {
			if (item.getCode().equals(code)) {
				return item;
			}
		}
		return null;
	}
	
	public static ResultCode of(BusinessException businessException) {
		ResultCode resultCode = ResultCode.FAIL;
		resultCode.code = businessException.getCode();
		resultCode.message = businessException.getMessage();
		return resultCode;
	}
}
