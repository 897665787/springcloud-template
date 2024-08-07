package com.company.tool.amqp.rabbitmq;

public interface Constants {

	String PREFIX = "tool.";

	interface EXCHANGE {
		String DIRECT = PREFIX + "direct";
		String XDELAYED = PREFIX + "x-delayed.direct";
	}

	interface QUEUE {
		// x-delayed 插件实现的延时队列
		interface XDELAYED {
			String NAME = PREFIX + "x-delayed";
			String ROUTING_KEY = PREFIX + "routingkey.x-delayed";
		}

		// x-dead-letter 2个队列实现的延时队列
		interface DEAD_LETTER {
			String NAME = PREFIX + "x-dead-letter";
			String ROUTING_KEY = PREFIX + "routingkey.x-dead-letter";
		}

		// 公共队列
		interface COMMON {
			String NAME = PREFIX + "common";
			String ROUTING_KEY = PREFIX + "routingkey.common";
		}

		// 发送短信
		interface SEND_SMS {
			String NAME = PREFIX + "send_sms";
			String ROUTING_KEY = PREFIX + "routingkey.send_sms";
		}
		
		// 发送邮件
		interface SEND_EMAIL {
			String NAME = PREFIX + "send_email";
			String ROUTING_KEY = PREFIX + "routingkey.send_email";
		}
		
		// 发送企微机器人信息
		interface SEND_WEBHOOK {
			String NAME = PREFIX + "send_webhook";
			String ROUTING_KEY = PREFIX + "routingkey.send_webhook";
		}
		
		// 发送订阅消息
		interface SEND_SUBSCRIBE {
			String NAME = PREFIX + "send_subscribe";
			String ROUTING_KEY = PREFIX + "routingkey.send_subscribe";
		}

	}
}
