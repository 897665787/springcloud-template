package com.company.tool.amqp.rabbitmq.consumer;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Argument;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import com.company.framework.amqp.rabbit.utils.ConsumerUtils;
import com.company.tool.amqp.Constants;
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