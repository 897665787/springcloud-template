package com.company.zuul.amqp.rabbit.constants;

/**
 * Fanout广播事件一般基于交换机来做
 */
public interface FanoutConstants {

	String PREFIX = "fanout.";

	// 部署事件
	interface DEPLOY {
		// 交换机
		String EXCHANGE = PREFIX + "deploy";
	}
}
