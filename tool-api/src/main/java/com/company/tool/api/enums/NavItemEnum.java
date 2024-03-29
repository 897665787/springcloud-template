package com.company.tool.api.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

public interface NavItemEnum {

	@AllArgsConstructor
	enum Status {
		// 状态(off:下架,on:上架)
		ON("on", "上架"), OFF("off", "下架");

		@Getter
		private String code;

		@Getter
		private String desc;

		public static Status of(String code) {
			for (Status item : Status.values()) {
				if (item.getCode().equals(code)) {
					return item;
				}
			}
			return null;
		}
	}

	@AllArgsConstructor
	enum Type {
		// 类型(redirect_http:跳转http链接,redirect_mini:跳转小程序(APP)页面,redirect_other_mini:跳转其他小程序链接)
		redirect_http("redirect_http", "跳转http链接"), //
		redirect_mini("redirect_mini", "跳转小程序(APP)页面"), //
		redirect_other_mini("redirect_other_mini", "跳转其他小程序链接"),//
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
