package com.company.web.messagedriven;

import com.company.framework.context.SpringContextUtil;
import com.company.framework.messagedriven.properties.Messagedriven;
import com.company.framework.messagedriven.properties.MessagedrivenProperties;

import java.util.Objects;

/**
 * EXCHANGE: rabbitmq对应为EXCHANGE，rocketmq对应为topic
 * KEY: rabbitmq对应为routingkey，rocketmq对应为tag
 */
public abstract class Constants {
    static MessagedrivenProperties messagedrivenProperties;
    static Messagedriven.Exchange exchange;
    static Messagedriven.Queue queue;

    static {
        messagedrivenProperties = SpringContextUtil.getBean(MessagedrivenProperties.class);
        assert messagedrivenProperties != null;
        exchange = messagedrivenProperties.getExchange();
        queue = messagedrivenProperties.getQueue();
    }

	interface EXCHANGE {
//		String DIRECT = PREFIX + "direct";
//      String XDELAYED = PREFIX + "x-delayed-direct";

		String DIRECT = exchange.getDirect();
		String XDELAYED = exchange.getXdelayed();
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
	}
}
