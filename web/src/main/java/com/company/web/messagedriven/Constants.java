package com.company.web.messagedriven;

/**
 * EXCHANGE: rabbitmq对应为EXCHANGE，rocketmq对应为topic
 * KEY: rabbitmq对应为routingkey，rocketmq对应为tag
 */
public interface Constants {

	String PREFIX = "web-";

	interface QUEUE {

		// 秒杀队列
		interface FLASH_KILL {
			String NAME = PREFIX + "flash_kill";
			String KEY = PREFIX + "key-flash_kill";
		}
	}
}
