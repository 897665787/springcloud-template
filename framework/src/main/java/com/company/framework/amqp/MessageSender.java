package com.company.framework.amqp;

public interface MessageSender {

	/**
	 * 发送普通消息
	 * 
	 * @param strategyName
	 * @param toJson
	 * @param exchange
	 * @param routingKey
	 */
	void sendNormalMessage(String strategyName, Object toJson, String exchange, String routingKey);

	/**
	 * 发送广播消息
	 * 
	 * @param toJson
	 * @param exchange
	 */
	void sendFanoutMessage(Object toJson, String exchange);

	/**
	 * 发送延时消息
	 * 
	 * @param strategyName
	 * @param toJson
	 * @param exchange
	 * @param routingKey
	 * @param delaySeconds
	 */
	void sendDelayMessage(String strategyName, Object toJson, String exchange, String routingKey, Integer delaySeconds);
}