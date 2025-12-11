package com.company.user.api.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

public interface UserOauthEnum {

	@AllArgsConstructor
	enum IdentityType {
        // 密码只有1个，不能绑定在USERNAME、MOBILE或EMAIL其中一条上，所以给密码单独建一个PASSWORD类型
        PASSWORD("password", "密码"),
		// 账号类型(mobile:手机号,wx-unionid:微信unionid,wx-openid-miniapp:微信小程序openid,wx-openid-mp:微信公众号openid,email:邮箱,username:用户名,sina:新浪微博,qq:QQ)
		USERNAME("username", "用户名"),
		MOBILE("mobile", "手机号"), 
		
		WX_UNIONID("wx-unionid", "微信unionid"),
		WX_OPENID_APP("wx-openid-app", "微信APP openid"),
		WX_OPENID_MINIAPP("wx-openid-miniapp", "微信小程序openid"),
		WX_OPENID_MP("wx-openid-mp", "微信公众号openid"),
		
		ALI_USERID_MINIAPP("ali-userid-miniapp", "支付宝小程序userId"),
		
		EMAIL("email", "邮箱"),
		SINA("sina", "新浪微博"),
		QQ("qq", "QQ"),
		;

		@Getter
		private String code;

		@Getter
		private String desc;

		public static IdentityType of(String code) {
			for (IdentityType item : IdentityType.values()) {
				if (item.getCode().equals(code)) {
					return item;
				}
			}
			return null;
		}
	}
}
