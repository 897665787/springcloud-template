package com.company.user.amqp.rabbitmq.consumer;

import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import com.company.framework.amqp.rabbit.constants.FanoutConstants;
import com.company.framework.amqp.rabbit.constants.HeaderConstants;
import com.company.framework.amqp.rabbit.utils.ConsumerUtils;
import com.company.user.amqp.strategy.StrategyConstants;
import com.rabbitmq.client.Channel;

@Component
public class UserRegisterConsumer {
	
	@RabbitListener(bindings = @QueueBinding(value = @Queue(value = FanoutConstants.USER_REGISTER.BIND_EMAIL_QUEUE, durable = "false", autoDelete = "true"), exchange = @Exchange(value = FanoutConstants.USER_REGISTER.EXCHANGE, type = ExchangeTypes.FANOUT, durable = "false", autoDelete = "true")))
	public void bindEmail(String jsonStrMsg, Channel channel, Message message) {
		message.getMessageProperties().setHeader(HeaderConstants.HEADER_STRATEGY_NAME,
				StrategyConstants.BINDEMAIL_STRATEGY);
		ConsumerUtils.handleByStrategy(jsonStrMsg, channel, message);
	}

	@RabbitListener(bindings = @QueueBinding(value = @Queue(value = FanoutConstants.USER_REGISTER.BIND_MOBILE_QUEUE, durable = "false", autoDelete = "true"), exchange = @Exchange(value = FanoutConstants.USER_REGISTER.EXCHANGE, type = ExchangeTypes.FANOUT, durable = "false", autoDelete = "true")))
	public void bindMobile(String jsonStrMsg, Channel channel, Message message) {
		message.getMessageProperties().setHeader(HeaderConstants.HEADER_STRATEGY_NAME,
				StrategyConstants.BINDMOBILE_STRATEGY);
		ConsumerUtils.handleByStrategy(jsonStrMsg, channel, message);
	}
	
}