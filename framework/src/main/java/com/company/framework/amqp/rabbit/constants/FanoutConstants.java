package com.company.framework.amqp.rabbit.constants;

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

	// 订单创建事件
	interface ORDER_CREATE {
		// 交换机
		String EXCHANGE = FanoutConstants.PREFIX + "order_create";

		String PREFIX = FanoutConstants.PREFIX + "order_create.";
		// 队列
		String SMS_QUEUE = PREFIX + "sms";
		String COUNTMONEY_QUEUE = PREFIX + "countmoney";
	}
	
	// canal事件
	interface CANAL {
		// 交换机
		String EXCHANGE = FanoutConstants.PREFIX + "canal";
		
		String PREFIX = FanoutConstants.PREFIX + "canal.";
		// 队列
		String USER_QUEUE = PREFIX + "user";
		String ORDER_QUEUE = PREFIX + "order";
	}
}
