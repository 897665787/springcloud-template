package com.company.tool.messagedriven;

/**
 * EXCHANGE: rabbitmq对应为EXCHANGE，rocketmq对应为topic
 * KEY: rabbitmq对应为routingkey，rocketmq对应为tag
 */
public interface Constants {

	String PREFIX = "template-tool";// 建议与项目名一致

	interface EXCHANGE {
		// 可定义自己的交换机，也可以使用framework定义的交换机
	}

	interface QUEUE {
		// 可定义自己的队列，也可以使用framework定义的队列
		// 发送短信
		interface SEND_SMS {
			String NAME = PREFIX + "send_sms";
			String KEY = PREFIX + "key-send_sms";
		}

		// 发送邮件
		interface SEND_EMAIL {
			String NAME = PREFIX + "send_email";
			String KEY = PREFIX + "key-send_email";
		}

		// 发送企微机器人信息
		interface SEND_WEBHOOK {
			String NAME = PREFIX + "send_webhook";
			String KEY = PREFIX + "key-send_webhook";
		}

		// 发送订阅消息
		interface SEND_SUBSCRIBE {
			String NAME = PREFIX + "send_subscribe";
			String KEY = PREFIX + "key-send_subscribe";
		}
	}
}
