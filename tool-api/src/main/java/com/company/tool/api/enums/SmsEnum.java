package com.company.tool.api.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

public interface SmsEnum {

	@AllArgsConstructor
	enum Type {
		// verifycode:验证码,market:营销活动,tips:提示信息
		VERIFYCODE("verifycode", "验证码"), //
		MARKET("market", "营销活动"), //
		TIPS("tips", "提示信息"), //
		REMIND_PASSWORD_EXPIRE("rd_pass_exp", "提醒密码过期"),//
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
