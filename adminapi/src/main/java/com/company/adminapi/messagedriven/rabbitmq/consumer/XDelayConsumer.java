package com.company.adminapi.messagedriven.rabbitmq.consumer;

import com.company.framework.messagedriven.rabbitmq.RabbitMQAutoConfiguration;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Argument;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.context.annotation.Conditional;
import org.springframework.stereotype.Component;

import com.company.adminapi.messagedriven.Constants;
import com.company.framework.messagedriven.rabbitmq.utils.ConsumerUtils;
import com.rabbitmq.client.Channel;

/**
 * 延时队列消费者(用x-delayed-message插件实现)
 *
 * <pre>
 * 可以解决高延时消息阻塞低延时消息问题
 * </pre>
 *
 * @return
 */
@Component
@Conditional(RabbitMQAutoConfiguration.RabbitMQCondition.class)
public class XDelayConsumer {

	@RabbitListener(
			bindings = @QueueBinding(value = @Queue(value = "${messagedriven.queue.xdelayed.name}"),
			exchange = @Exchange(value = "${messagedriven.exchange.xdelayed}", type = "x-delayed-message",
								arguments = { @Argument(name = "x-delayed-type", value = "direct", type = "java.lang.String") }),
			key = "${messagedriven.queue.xdelayed.key}"))
	public void handle(String jsonStrMsg, Channel channel, Message message) {
		ConsumerUtils.handleByStrategy(jsonStrMsg, channel, message);
	}
}
