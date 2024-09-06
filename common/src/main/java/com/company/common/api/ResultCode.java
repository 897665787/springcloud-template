package com.company.common.api;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum ResultCode {
	SUCCESS(0, "成功"), //
	FAIL(99, "失败"), //
	/* 框架错误码 */
	SYSTEM_ERROR(500, "系统错误"), //
	API_FUSING(503, "API熔断"), //
	NO_LOGIN(302, "未登录"), //
	/* 自定义业务错误码(4位) */
	NO_PERMISSION(1002, "未授权"), //
	PARAM_INVALID(1003, "参数无效"), //
	LOGIN_NEED_MOBILE(9999, "登录需要绑定手机号"), //
	;

	@Getter
	private Integer code;
	@Getter
	private String message;

	public static ResultCode of(Integer code) {
		for (ResultCode item : ResultCode.values()) {
			if (item.getCode().equals(code)) {
				return item;
			}
		}
		return null;
	}
}
