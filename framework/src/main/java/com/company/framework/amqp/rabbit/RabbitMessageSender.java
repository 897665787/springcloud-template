package com.company.framework.amqp.rabbit;

import java.util.Map;

import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.support.CorrelationData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import com.company.common.util.JsonUtil;
import com.company.framework.amqp.MessageSender;
import com.company.framework.autoconfigure.RabbitAutoConfiguration.RabbitCondition;
import com.company.framework.filter.MdcUtil;

import cn.hutool.core.util.RandomUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Primary
@Component
@Conditional(RabbitCondition.class)
public class RabbitMessageSender implements MessageSender {

	@Autowired(required = false)
	private RabbitTemplate rabbitTemplate;

	/**
	 * 发送消息
	 * 
	 * @param params
	 * @param exchange
	 * @param routingKey
	 */
	@Override
	public void sendMessage(Map<String, Object> params, String exchange, String routingKey) {
		sendMessage(params, exchange, routingKey, null);
	}

	/**
	 * 发送消息
	 * 
	 * @param params
	 * @param exchange
	 * @param routingKey
	 * @param delaySeconds
	 */
	@Override
	public void sendMessage(Map<String, Object> params, String exchange, String routingKey, Integer delaySeconds) {
		if (rabbitTemplate == null) {
			log.warn("rabbitTemplate not init");
			return;
		}

		MessageContent messageContent = new MessageContent();
		messageContent.setParams(params);

		String correlationId = RandomUtil.simpleUUID();
		rabbitTemplate.convertAndSend(exchange, routingKey, messageContent, msg -> {
			MessageProperties messageProperties = msg.getMessageProperties();
			if (delaySeconds != null) {
				Long delayMillis = delaySeconds * 1000L;// 设置延时毫秒值
				messageProperties.setExpiration(String.valueOf(delayMillis));// 单位：毫秒
			}
			messageProperties.setMessageId(MdcUtil.get());
			return msg;
		}, new CorrelationData(correlationId));
		log.info("convertAndSend,correlationId:{},messageContent:{},exchange:{},routingKey:{},delaySeconds:{}",
				correlationId, JsonUtil.toJsonString(messageContent), exchange, routingKey, delaySeconds);
	}
}