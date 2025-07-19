package com.company.order.messagedriven;

public interface Constants {

	String PREFIX = "order-";

	interface EXCHANGE {
		String DIRECT = PREFIX + "direct";
		String XDELAYED = PREFIX + "x-delayed-direct";
	}

	interface QUEUE {
		// x-delayed 插件实现的延时队列
		interface XDELAYED {
			String NAME = PREFIX + "x-delayed";
			String KEY = PREFIX + "key-x-delayed";
		}

		// x-dead-letter 2个队列实现的延时队列
		interface DEAD_LETTER {
			String NAME = PREFIX + "x-dead-letter";
			String KEY = PREFIX + "key-x-dead-letter";
		}

		// 公共队列
		interface COMMON {
			String NAME = PREFIX + "common";
			String KEY = PREFIX + "key-common";
		}

		// 秒杀队列
		interface FLASH_KILL {
			String NAME = PREFIX + "flash_kill";
			String KEY = PREFIX + "key-flash_kill";
		}
		
		// 支付回调队列
		interface PAY_NOTIFY {
			String NAME = PREFIX + "pay_notify";
			String KEY = PREFIX + "key-pay_notify";
		}
	}
}
