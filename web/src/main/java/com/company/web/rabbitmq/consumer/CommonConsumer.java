package com.company.web.rabbitmq.consumer;

import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.context.annotation.Conditional;
import org.springframework.stereotype.Component;

import com.company.common.util.JsonUtil;
import com.company.framework.amqp.rabbit.MessageContent;
import com.company.framework.autoconfigure.RabbitAutoConfiguration.RabbitCondition;
import com.company.framework.filter.MdcUtil;
import com.company.web.rabbitmq.QueueConstant;
import com.rabbitmq.client.Channel;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@Conditional(RabbitCondition.class)
public class CommonConsumer {

	@RabbitListener(bindings = @QueueBinding(value = @Queue(value = QueueConstant.COMMON_QUEUE.QUEUE), exchange = @Exchange(value = QueueConstant.EXCHANGE, type = ExchangeTypes.DIRECT), key = QueueConstant.COMMON_QUEUE.ROUTING_KEY))
	public void handle(MessageContent messageContent, Channel channel, Message message) {
		
		try {
			MessageProperties messageProperties = message.getMessageProperties();
			if (messageProperties == null) {
				return;
			}
			MdcUtil.put(messageProperties.getMessageId());

			log.info("messageContent:{}", JsonUtil.toJsonString(messageContent));

			channel.basicAck(messageProperties.getDeliveryTag(), false);
		} catch (Exception e) {
			log.error("handle message error", e);
		}
	}
}