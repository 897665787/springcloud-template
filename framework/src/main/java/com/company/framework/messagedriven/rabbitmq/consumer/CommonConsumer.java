package com.company.framework.messagedriven.rabbitmq.consumer;

import com.company.framework.messagedriven.rabbitmq.RabbitMQAutoConfiguration;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.context.annotation.Conditional;
import org.springframework.stereotype.Component;

import com.company.framework.messagedriven.rabbitmq.utils.ConsumerUtils;
import com.rabbitmq.client.Channel;

@Component
@Conditional(RabbitMQAutoConfiguration.RabbitMQCondition.class)
public class CommonConsumer {

	@RabbitListener(bindings = @QueueBinding(value = @Queue(value = "${messagedriven.queue.common.name}"), exchange = @Exchange(value = "${messagedriven.exchange.direct}"), key = "${messagedriven.queue.common.key}"))
	public void handle(String jsonStrMsg, Channel channel, Message message) {
		ConsumerUtils.handleByStrategy(jsonStrMsg, channel, message);
	}
}
