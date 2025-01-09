package com.company.tool.amqp.rabbitmq.consumer;

import com.company.framework.autoconfigure.RabbitAutoConfiguration;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.context.annotation.Conditional;
import org.springframework.stereotype.Component;

import com.company.framework.amqp.rabbit.utils.ConsumerUtils;
import com.company.tool.amqp.Constants;
import com.rabbitmq.client.Channel;

@Component
@Conditional(RabbitAutoConfiguration.RabbitCondition.class)
public class SendSubscribeConsumer {

	@RabbitListener(bindings = @QueueBinding(value = @Queue(value = Constants.QUEUE.SEND_SUBSCRIBE.NAME), exchange = @Exchange(value = Constants.EXCHANGE.DIRECT), key = Constants.QUEUE.SEND_SUBSCRIBE.KEY))
	public void handle(String jsonStrMsg, Channel channel, Message message) {
		ConsumerUtils.handleByStrategy(jsonStrMsg, channel, message);
	}
}