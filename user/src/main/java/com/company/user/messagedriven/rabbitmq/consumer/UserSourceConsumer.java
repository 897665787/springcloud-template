package com.company.user.messagedriven.rabbitmq.consumer;

import com.company.framework.messagedriven.constants.HeaderConstants;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import com.company.framework.messagedriven.constants.FanoutConstants;
import com.company.framework.messagedriven.rabbitmq.utils.ConsumerUtils;
import com.company.user.messagedriven.strategy.StrategyConstants;
import com.rabbitmq.client.Channel;

@Component
public class UserSourceConsumer {

	@RabbitListener(bindings = @QueueBinding(value = @Queue(value = FanoutConstants.USER_SOURCE.SOURCE_RECORD_QUEUE, durable = "false", autoDelete = "true"), exchange = @Exchange(value = FanoutConstants.USER_SOURCE.EXCHANGE, type = ExchangeTypes.FANOUT, durable = "false", autoDelete = "true")))
	public void sourceRecord(String jsonStrMsg, Channel channel, Message message) {
		message.getMessageProperties().setHeader(HeaderConstants.HEADER_STRATEGY_NAME,
				StrategyConstants.SOURCERECORD_STRATEGY);
		ConsumerUtils.handleByStrategy(jsonStrMsg, channel, message);
	}
}