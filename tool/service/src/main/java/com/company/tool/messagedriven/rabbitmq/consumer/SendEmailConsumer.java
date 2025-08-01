package com.company.tool.messagedriven.rabbitmq.consumer;

import com.company.framework.messagedriven.rabbitmq.RabbitMQAutoConfiguration;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.context.annotation.Conditional;
import org.springframework.stereotype.Component;

import com.company.framework.messagedriven.rabbitmq.utils.ConsumerUtils;
import com.company.tool.messagedriven.Constants;
import com.rabbitmq.client.Channel;

@Component
@Conditional(RabbitMQAutoConfiguration.RabbitMQCondition.class)
public class SendEmailConsumer {

	@RabbitListener(bindings = @QueueBinding(value = @Queue(value = Constants.QUEUE.SEND_EMAIL.NAME), exchange = @Exchange(value = Constants.EXCHANGE.DIRECT), key = Constants.QUEUE.SEND_EMAIL.KEY))
	public void handle(String jsonStrMsg, Channel channel, Message message) {
		ConsumerUtils.handleByStrategy(jsonStrMsg, channel, message);
	}
}
