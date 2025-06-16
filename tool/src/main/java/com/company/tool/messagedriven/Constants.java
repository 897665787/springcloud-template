package com.company.tool.messagedriven;

public interface Constants {

	String PREFIX = "tool-";

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

		// 发送推送
		interface SEND_PUSH {
			String NAME = PREFIX + "send_push";
			String KEY = PREFIX + "key-send_push";
		}
	}
}
