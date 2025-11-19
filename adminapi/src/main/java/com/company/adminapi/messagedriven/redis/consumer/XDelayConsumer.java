package com.company.adminapi.messagedriven.redis.consumer;

import com.company.framework.messagedriven.rabbitmq.RabbitMQAutoConfiguration;
import com.company.framework.messagedriven.rabbitmq.utils.ConsumerUtils;
import com.company.adminapi.messagedriven.Constants;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.context.annotation.Conditional;
import org.springframework.stereotype.Component;

@Component
@Conditional(RabbitMQAutoConfiguration.RabbitMQCondition.class)
public class XDelayConsumer {

	@RabbitListener(
			bindings = @QueueBinding(value = @Queue(value = Constants.QUEUE.XDELAYED.NAME),
			exchange = @Exchange(value = Constants.EXCHANGE.XDELAYED, type = "x-delayed-message",
								arguments = { @Argument(name = "x-delayed-type", value = "direct", type = "java.lang.String") }),
			key = Constants.QUEUE.XDELAYED.KEY))
	public void handle(String jsonStrMsg, Channel channel, Message message) {
		ConsumerUtils.handleByStrategy(jsonStrMsg, channel, message);
	}
}
