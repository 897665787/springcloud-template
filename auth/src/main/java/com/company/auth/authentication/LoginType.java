package com.company.auth.authentication;

import lombok.AllArgsConstructor;
import lombok.Getter;

public interface LoginType {
	String LOCAL_MOBILE = "localMobile";
	String USERNAME_PASSWORD = "usernamePassword";
	String MOBILE_CODE = "mobileCode";
	String MOBILE_CODE_BIND = "mobileCodeBind";
	
	String APP_WEIXIN = "appWeixin";
	String WEIXIN_MINIAPP = "weixinMiniapp";
	String WEIXIN_MP = "weixinMp";

	@AllArgsConstructor
	enum Enum {
		LOCAL_MOBILE            (LoginType.LOCAL_MOBILE            	, "本机号码一键登录"),
		USERNAME_PASSWORD       (LoginType.USERNAME_PASSWORD        , "用户名密码登录"),
		MOBILE_CODE             (LoginType.MOBILE_CODE              , "手机号验证码登录"),
		MOBILE_CODE_BIND  		(LoginType.MOBILE_CODE_BIND   		, "手机号验证码登录并绑定授权码"),
		
		APP_WEIXIN              (LoginType.APP_WEIXIN               , "APP微信授权登录"),
		WEIXIN_MINIAPP          (LoginType.WEIXIN_MINIAPP           , "微信小程序登录"),
		WEIXIN_MP               (LoginType.WEIXIN_MP                , "微信公众号登录"),
	    ;
		
		@Getter
	    private String type;
		@Getter
	    private String message;
		
		public static Enum of(String type) {
			Enum[] values = Enum.values();
			for (Enum item : values) {
				if (item.getType().equals(type)) {
					return item;
				}
			}
			return null;
		}
	}
}
