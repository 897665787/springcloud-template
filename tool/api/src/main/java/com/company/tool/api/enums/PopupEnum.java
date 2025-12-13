package com.company.tool.api.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

public interface PopupEnum {

	@AllArgsConstructor
	enum Model {
		/** 这里的值需前后端一起约定，需与数据库的值保持一致（注意大小写匹配） */
		// 弹窗模板(simple:简单弹窗(图片+关闭按钮+确认按钮),newuser_coupon:团餐新人券包)
		simple("simple", "简单弹窗"), // 图片+关闭按钮+确认按钮
		newuser_coupon("newuser_coupon", "新人券包"),// 需展示优惠券模板的内容
		;

		@Getter
		private String code;

		@Getter
		private String desc;

		public static Model of(String code) {
			for (Model item : Model.values()) {
				if (item.getCode().equals(code)) {
					return item;
				}
			}
			return null;
		}
	}

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
	enum LogBusinessType {
		// 业务类型(popup:popup,user_popup:user_popup)
		POPUP("popup", "popup"), USER_POPUP("user_popup", "user_popup");
		
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
		// 类型(close:关闭,api:请求API,subscribe:订阅消息,redirect_http:跳转http链接,redirect_mini:跳转小程序链接,redirect_other_mini:跳转其他小程序链接)
		close("close", "关闭"),
		api("api", "请求API"),
		subscribe("subscribe", "订阅消息"),
		subscribe_auto("subscribe_auto", "订阅消息-自动拉起"), // 不能设置到最外层弹窗
		redirect_http("redirect_http", "跳转http链接"),
		redirect_mini("redirect_mini", "跳转小程序链接"),
		redirect_other_mini("redirect_other_mini", "跳转其他小程序链接"),
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
