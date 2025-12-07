package com.company.order.messagedriven.rabbitmq.consumer;

import com.company.framework.messagedriven.rabbitmq.RabbitMQAutoConfiguration;
import com.company.framework.messagedriven.rabbitmq.utils.ConsumerUtils;
import com.company.order.messagedriven.Constants;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.context.annotation.Conditional;
import org.springframework.stereotype.Component;

@Component
@Conditional(RabbitMQAutoConfiguration.RabbitMQCondition.class)
public class PayNotifyConsumer {

	@RabbitListener(
            bindings = @QueueBinding(value = @Queue(value = Constants.QUEUE.PAY_NOTIFY.NAME),
                    exchange = @Exchange(value = "${messagedriven.exchange.direct}"),
                    key = Constants.QUEUE.PAY_NOTIFY.KEY))
	public void handle(String jsonStrMsg, Channel channel, Message message) {
		ConsumerUtils.handleByStrategy(jsonStrMsg, channel, message);
	}
}
