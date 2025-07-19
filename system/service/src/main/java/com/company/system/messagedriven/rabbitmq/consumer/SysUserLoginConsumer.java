package com.company.system.messagedriven.rabbitmq.consumer;

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

import com.rabbitmq.client.Channel;
import com.company.framework.messagedriven.constants.FanoutConstants;
import com.company.framework.messagedriven.rabbitmq.utils.ConsumerUtils;
import com.company.system.messagedriven.strategy.StrategyConstants;

@Component
@Conditional(RabbitMQAutoConfiguration.RabbitMQCondition.class)
public class SysUserLoginConsumer {

	@RabbitListener(bindings = @QueueBinding(value = @Queue(value = FanoutConstants.SYS_USER_LOGIN.SYS_LOGIN_RECORD_QUEUE, durable = "false", autoDelete = "true"), exchange = @Exchange(value = FanoutConstants.SYS_USER_LOGIN.EXCHANGE, type = ExchangeTypes.FANOUT, durable = "false", autoDelete = "true")))
	public void loginRecord(String jsonStrMsg, Channel channel, Message message) {
		message.getMessageProperties().setHeader(HeaderConstants.HEADER_STRATEGY_NAME,
				StrategyConstants.SYS_LOGINRECORD_STRATEGY);
		ConsumerUtils.handleByStrategy(jsonStrMsg, channel, message);
	}

	@RabbitListener(bindings = @QueueBinding(value = @Queue(value = FanoutConstants.SYS_USER_LOGIN.INCR_EXPIRELOGINTIMES_QUEUE, durable = "false", autoDelete = "true"), exchange = @Exchange(value = FanoutConstants.SYS_USER_LOGIN.EXCHANGE, type = ExchangeTypes.FANOUT, durable = "false", autoDelete = "true")))
	public void incrExpireLoginTimes(String jsonStrMsg, Channel channel, Message message) {
		message.getMessageProperties().setHeader(HeaderConstants.HEADER_STRATEGY_NAME,
				StrategyConstants.INCR_EXPIRELOGINTIMES_STRATEGY);
		ConsumerUtils.handleByStrategy(jsonStrMsg, channel, message);
	}
}
