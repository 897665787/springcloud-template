package com.company.user.amqp.rabbitmq.consumer;

import com.company.framework.amqp.constants.HeaderConstants;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import com.company.framework.amqp.constants.FanoutConstants;
import com.company.framework.amqp.rabbit.utils.ConsumerUtils;
import com.company.user.amqp.strategy.StrategyConstants;
import com.rabbitmq.client.Channel;

@Component
public class RefundApplyResultConsumer {

	@RabbitListener(bindings = @QueueBinding(value = @Queue(value = FanoutConstants.REFUND_APPLY_RESULT.MEMBER_BUY_QUEUE, durable = "false", autoDelete = "true"), exchange = @Exchange(value = FanoutConstants.REFUND_APPLY_RESULT.EXCHANGE, type = ExchangeTypes.FANOUT, durable = "false", autoDelete = "true")))
	public void memberBuy(String jsonStrMsg, Channel channel, Message message) {
		message.getMessageProperties().setHeader(HeaderConstants.HEADER_STRATEGY_NAME,
				StrategyConstants.MEMBER_BUY_STRATEGY);
		ConsumerUtils.handleByStrategy(jsonStrMsg, channel, message);
	}

	@RabbitListener(bindings = @QueueBinding(value = @Queue(value = FanoutConstants.REFUND_APPLY_RESULT.GOODS_REFUND_QUEUE, durable = "false", autoDelete = "true"), exchange = @Exchange(value = FanoutConstants.REFUND_APPLY_RESULT.EXCHANGE, type = ExchangeTypes.FANOUT, durable = "false", autoDelete = "true")))
	public void goodsRefund(String jsonStrMsg, Channel channel, Message message) {
		message.getMessageProperties().setHeader(HeaderConstants.HEADER_STRATEGY_NAME,
				StrategyConstants.GOODS_REFUND_STRATEGY);
		ConsumerUtils.handleByStrategy(jsonStrMsg, channel, message);
	}

	@RabbitListener(bindings = @QueueBinding(value = @Queue(value = FanoutConstants.REFUND_APPLY_RESULT.RECHARGE_REFUND_QUEUE, durable = "false", autoDelete = "true"), exchange = @Exchange(value = FanoutConstants.REFUND_APPLY_RESULT.EXCHANGE, type = ExchangeTypes.FANOUT, durable = "false", autoDelete = "true")))
	public void recharge(String jsonStrMsg, Channel channel, Message message) {
		message.getMessageProperties().setHeader(HeaderConstants.HEADER_STRATEGY_NAME,
				StrategyConstants.RECHARGE_STRATEGY);
		ConsumerUtils.handleByStrategy(jsonStrMsg, channel, message);
	}
}