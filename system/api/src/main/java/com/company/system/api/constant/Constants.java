package com.company.system.api.constant;

/**
 * 常量
 */
public interface Constants {
	/**
	 * 服务名
	 */
	String FEIGNCLIENT_VALUE = "template-system";

	static String feignUrl(String path) {
		return "http://" + FEIGNCLIENT_VALUE + path;
	}

	/** 管理员角色权限标识 */
    String SUPER_ROLE = "admin";
}
