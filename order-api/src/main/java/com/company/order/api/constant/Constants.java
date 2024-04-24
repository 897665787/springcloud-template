package com.company.order.api.constant;

/**
 * 常量
 */
public interface Constants {
	/**
	 * 服务名
	 */
	String FEIGNCLIENT_VALUE = "template-order";

	static String feignUrl(String path) {
		return "http://" + FEIGNCLIENT_VALUE + path;
	}
}
