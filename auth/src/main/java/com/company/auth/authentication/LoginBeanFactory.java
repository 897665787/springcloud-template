package com.company.auth.authentication;

import com.company.framework.context.SpringContextUtil;

public class LoginBeanFactory {
	private static final String SUFFIX = "Login";

	public static final String LOCAL_MOBILE = LoginType.LOCAL_MOBILE + SUFFIX;
	public static final String USERNAME_PASSWORD = LoginType.USERNAME_PASSWORD + SUFFIX;
	public static final String MOBILE_CODE = LoginType.MOBILE_CODE + SUFFIX;
	public static final String MOBILE_CODE_BIND = LoginType.MOBILE_CODE_BIND + SUFFIX;
	
	public static final String APP_WEIXIN = LoginType.APP_WEIXIN + SUFFIX;
	public static final String WEIXIN_MINIAPP = LoginType.WEIXIN_MINIAPP + SUFFIX;
	public static final String WEIXIN_MINIAPP_MOBILE = LoginType.WEIXIN_MINIAPP_MOBILE + SUFFIX;
	public static final String WEIXIN_MP = LoginType.WEIXIN_MP + SUFFIX;

	public static LoginService of(LoginType.Enum loginType) {
		String beanName = loginType.getType() + SUFFIX;
		LoginService secondsStrategy = SpringContextUtil.getBean(beanName, LoginService.class);
		return secondsStrategy;
	}
}
