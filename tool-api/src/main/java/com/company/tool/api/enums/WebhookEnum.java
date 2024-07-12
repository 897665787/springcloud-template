package com.company.tool.api.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

public interface WebhookEnum {

	@AllArgsConstructor
	enum Type {
		// systemerror:系统异常,xx_send_fail:xx发货失败
		SYSTEM_ERROR("system_error", "系统异常"),XX_SEND_FAIL("xx_send_fail", "xx发货失败");

		@Getter
		private String code;
		@Getter
		private String desc;

		public static Type of(String code) {
			for (Type item : Type.values()) {
				if (item.getCode().equals(code)) {
					return item;
				}
			}
			return null;
		}
	}
}
