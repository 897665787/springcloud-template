package com.company.user.messagedriven.rabbitmq.consumer;

import com.company.framework.messagedriven.constants.HeaderConstants;
import com.company.framework.messagedriven.rabbitmq.RabbitMQAutoConfiguration;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.context.annotation.Conditional;
import org.springframework.stereotype.Component;

import com.company.framework.messagedriven.constants.BroadcastConstants;
import com.company.framework.messagedriven.rabbitmq.utils.ConsumerUtils;
import com.company.user.messagedriven.strategy.StrategyConstants;
import com.rabbitmq.client.Channel;

@Component
@Conditional(RabbitMQAutoConfiguration.RabbitMQCondition.class)
public class UserRegisterConsumer {

	@RabbitListener(bindings = @QueueBinding(value = @Queue(value = BroadcastConstants.USER_REGISTER.BIND_EMAIL_QUEUE, durable = "false", autoDelete = "true"), exchange = @Exchange(value = BroadcastConstants.USER_REGISTER.EXCHANGE, type = ExchangeTypes.FANOUT, durable = "false", autoDelete = "true")))
	public void bindEmail(String jsonStrMsg, Channel channel, Message message) {
		message.getMessageProperties().setHeader(HeaderConstants.HEADER_STRATEGY_NAME,
				StrategyConstants.BINDEMAIL_STRATEGY);
		ConsumerUtils.handleByStrategy(jsonStrMsg, channel, message);
	}

	@RabbitListener(bindings = @QueueBinding(value = @Queue(value = BroadcastConstants.USER_REGISTER.BIND_MOBILE_QUEUE, durable = "false", autoDelete = "true"), exchange = @Exchange(value = BroadcastConstants.USER_REGISTER.EXCHANGE, type = ExchangeTypes.FANOUT, durable = "false", autoDelete = "true")))
	public void bindMobile(String jsonStrMsg, Channel channel, Message message) {
		message.getMessageProperties().setHeader(HeaderConstants.HEADER_STRATEGY_NAME,
				StrategyConstants.BINDMOBILE_STRATEGY);
		ConsumerUtils.handleByStrategy(jsonStrMsg, channel, message);
	}

}
