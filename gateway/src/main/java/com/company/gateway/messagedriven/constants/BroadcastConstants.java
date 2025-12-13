package com.company.gateway.messagedriven.constants;

/**
 * 广播事件常量
 */
public interface BroadcastConstants {

	String PREFIX = "broadcast";

	// 部署事件
	interface DEPLOY {
		// 交换机
		String EXCHANGE = PREFIX + "-deploy";
	}
}
