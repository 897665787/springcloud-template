package com.company.user.api.constant;

/**
 * 常量
 */
public interface Constants {
	/**
	 * 服务名
	 */
	String FEIGNCLIENT_VALUE = "template-user";

	static String feignUrl(String path) {
		return "http://" + FEIGNCLIENT_VALUE + path;
	}
}
