package com.company.framework.amqp;

import java.util.Map;

public interface MessageSender {

	/**
	 * 发送消息
	 * 
	 * @param params
	 * @param exchange
	 * @param routingKey
	 */
	void sendMessage(Map<String, Object> params, String exchange, String routingKey);

	void sendMessage(Map<String, Object> params, String exchange, String routingKey, Integer delaySeconds);
}