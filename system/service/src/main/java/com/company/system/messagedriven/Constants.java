package com.company.system.messagedriven;


/**
 * EXCHANGE: rabbitmq对应为EXCHANGE，rocketmq对应为topic
 * KEY: rabbitmq对应为routingkey，rocketmq对应为tag
 */
public interface Constants {

	String PREFIX = "template-system";// 建议与项目名一致

	interface EXCHANGE {
		// 可定义自己的交换机，也可以使用framework定义的交换机
	}

	interface QUEUE {
		// 可定义自己的队列，也可以使用framework定义的队列
		// 支付回调队列
		interface PAY_NOTIFY {
			String NAME = PREFIX + "-pay_notify";
			String KEY = PREFIX + "-key-pay_notify";
		}
	}
}
