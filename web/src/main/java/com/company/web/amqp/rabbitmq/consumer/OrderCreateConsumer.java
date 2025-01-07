package com.company.web.amqp.rabbitmq.consumer;

import com.company.framework.autoconfigure.RabbitAutoConfiguration;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.context.annotation.Conditional;
import org.springframework.stereotype.Component;

import com.company.framework.amqp.rabbit.constants.FanoutConstants;
import com.company.framework.amqp.rabbit.constants.HeaderConstants;
import com.company.framework.amqp.rabbit.utils.ConsumerUtils;
import com.company.web.amqp.strategy.StrategyConstants;
import com.rabbitmq.client.Channel;

@Component
@Conditional(RabbitAutoConfiguration.RabbitCondition.class)
public class OrderCreateConsumer {

	@RabbitListener(bindings = @QueueBinding(value = @Queue(value = FanoutConstants.ORDER_CREATE.SMS_QUEUE, durable = "false", autoDelete = "true"), exchange = @Exchange(value = FanoutConstants.ORDER_CREATE.EXCHANGE, type = ExchangeTypes.FANOUT, durable = "false", autoDelete = "true")))
	public void sms(String jsonStrMsg, Channel channel, Message message) {
		message.getMessageProperties().setHeader(HeaderConstants.HEADER_STRATEGY_NAME,
				StrategyConstants.ORDERCREATE_SMS_STRATEGY);
		ConsumerUtils.handleByStrategy(jsonStrMsg, channel, message);
	}

	@RabbitListener(bindings = @QueueBinding(value = @Queue(value = FanoutConstants.ORDER_CREATE.COUNTMONEY_QUEUE, durable = "false", autoDelete = "true"), exchange = @Exchange(value = FanoutConstants.ORDER_CREATE.EXCHANGE, type = ExchangeTypes.FANOUT, durable = "false", autoDelete = "true")))
	public void countmoney(String jsonStrMsg, Channel channel, Message message) {
		message.getMessageProperties().setHeader(HeaderConstants.HEADER_STRATEGY_NAME,
				StrategyConstants.ORDERCREATE_COUNTMONEY_STRATEGY);
		ConsumerUtils.handleByStrategy(jsonStrMsg, channel, message);
	}
}