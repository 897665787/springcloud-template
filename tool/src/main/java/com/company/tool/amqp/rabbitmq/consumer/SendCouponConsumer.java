package com.company.tool.amqp.rabbitmq.consumer;

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
import com.company.tool.amqp.strategy.StrategyConstants;
import com.rabbitmq.client.Channel;

/**
 * 发放优惠券消费者（订阅消息mq触发demo）
 */
@Component
public class SendCouponConsumer {

	// 优惠券到账提醒、优惠券发放通知
	@RabbitListener(bindings = @QueueBinding(value = @Queue(value = FanoutConstants.SEND_COUPON.SUBSCRIBE_RECEIVE_QUEUE, durable = "false", autoDelete = "true"), exchange = @Exchange(value = FanoutConstants.SEND_COUPON.EXCHANGE, type = ExchangeTypes.FANOUT, durable = "false", autoDelete = "true")))
	public void subscribeReceive(String jsonStrMsg, Channel channel, Message message) {
		message.getMessageProperties().setHeader(HeaderConstants.HEADER_STRATEGY_NAME,
				StrategyConstants.COUPON_RECEIVE_STRATEGY);
		ConsumerUtils.handleByStrategy(jsonStrMsg, channel, message);
	}

	// 优惠券使用提醒
	@RabbitListener(bindings = @QueueBinding(value = @Queue(value = FanoutConstants.SEND_COUPON.SUBSCRIBE_TOUSE_QUEUE, durable = "false", autoDelete = "true"), exchange = @Exchange(value = FanoutConstants.SEND_COUPON.EXCHANGE, type = ExchangeTypes.FANOUT, durable = "false", autoDelete = "true")))
	public void subscribeTouse(String jsonStrMsg, Channel channel, Message message) {
		message.getMessageProperties().setHeader(HeaderConstants.HEADER_STRATEGY_NAME,
				StrategyConstants.COUPON_TOUSE_STRATEGY);
		ConsumerUtils.handleByStrategy(jsonStrMsg, channel, message);
	}

	// 优惠券过期提醒
	@RabbitListener(bindings = @QueueBinding(value = @Queue(value = FanoutConstants.SEND_COUPON.SUBSCRIBE_EXPIRE_QUEUE, durable = "false", autoDelete = "true"), exchange = @Exchange(value = FanoutConstants.SEND_COUPON.EXCHANGE, type = ExchangeTypes.FANOUT, durable = "false", autoDelete = "true")))
	public void subscribeExpire(String jsonStrMsg, Channel channel, Message message) {
		message.getMessageProperties().setHeader(HeaderConstants.HEADER_STRATEGY_NAME,
				StrategyConstants.COUPON_EXPIRE_STRATEGY);
		ConsumerUtils.handleByStrategy(jsonStrMsg, channel, message);
	}
}