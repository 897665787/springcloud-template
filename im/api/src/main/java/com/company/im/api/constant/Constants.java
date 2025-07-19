package com.company.im.api.constant;

/**
 * 常量
 */
public interface Constants {
	/**
	 * 服务名
	 */
	String FEIGNCLIENT_VALUE = "template-im";

	static String feignUrl(String path) {
		return "http://" + FEIGNCLIENT_VALUE + path;
	}
}
