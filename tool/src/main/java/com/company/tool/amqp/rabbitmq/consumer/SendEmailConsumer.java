package com.company.tool.amqp.rabbitmq.consumer;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import com.company.framework.amqp.rabbit.utils.ConsumerUtils;
import com.company.tool.amqp.Constants;
import com.rabbitmq.client.Channel;

@Component
public class SendEmailConsumer {

	@RabbitListener(bindings = @QueueBinding(value = @Queue(value = Constants.QUEUE.SEND_EMAIL.NAME), exchange = @Exchange(value = Constants.EXCHANGE.DIRECT), key = Constants.QUEUE.SEND_EMAIL.KEY))
	public void handle(String jsonStrMsg, Channel channel, Message message) {
		ConsumerUtils.handleByStrategy(jsonStrMsg, channel, message);
	}
}