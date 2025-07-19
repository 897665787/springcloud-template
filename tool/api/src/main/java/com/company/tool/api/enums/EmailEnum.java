package com.company.tool.api.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

public interface EmailEnum {

	@AllArgsConstructor
	enum Type {
		// verifycode:验证码,market:营销活动,tips:提示信息
		VERIFYCODE("verifycode", "验证码"), //
		MARKET("market", "营销活动"), //
		TIPS("tips", "提示信息"), //
		REMIND_PASSWORD_EXPIRE("rd_pass_exp", "密码即将过期提醒"), //
		;

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
