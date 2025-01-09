package com.company.order.amqp;

public interface Constants {

	String PREFIX = "order-";

	interface EXCHANGE {
		String DIRECT = PREFIX + "direct";
		String XDELAYED = PREFIX + "x-delayed.direct";
	}

	interface QUEUE {
		// x-delayed 插件实现的延时队列
		interface XDELAYED {
			String NAME = PREFIX + "x-delayed";
			String KEY = PREFIX + "routingkey.x-delayed";
		}

		// x-dead-letter 2个队列实现的延时队列
		interface DEAD_LETTER {
			String NAME = PREFIX + "x-dead-letter";
			String KEY = PREFIX + "routingkey.x-dead-letter";
		}

		// 公共队列
		interface COMMON {
			String NAME = PREFIX + "common";
			String KEY = PREFIX + "routingkey.common";
		}

		// 秒杀队列
		interface FLASH_KILL {
			String NAME = PREFIX + "flash_kill";
			String KEY = PREFIX + "routingkey.flash_kill";
		}
		
		// 支付回调队列
		interface PAY_NOTIFY {
			String NAME = PREFIX + "pay_notify";
			String KEY = PREFIX + "routingkey.pay_notify";
		}
	}
}
