package com.company.system.amqp;

public interface Constants {

	String PREFIX = "system-";

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
	}
}
