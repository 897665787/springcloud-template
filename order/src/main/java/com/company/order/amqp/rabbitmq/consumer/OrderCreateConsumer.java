package com.company.order.amqp.rabbitmq.consumer;

import java.util.Map;
import java.util.function.Consumer;

import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.context.annotation.Conditional;
import org.springframework.stereotype.Component;

import com.company.common.util.JsonUtil;
import com.company.framework.amqp.rabbit.constants.FanoutConstants;
import com.company.framework.amqp.rabbit.utils.ConsumerUtils;
import com.company.framework.autoconfigure.RabbitAutoConfiguration.RabbitCondition;
import com.rabbitmq.client.Channel;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@Conditional(RabbitCondition.class)
public class OrderCreateConsumer {

	@RabbitListener(bindings = @QueueBinding(value = @Queue(value = FanoutConstants.ORDER_CREATE.SMS_QUEUE, durable = "false", autoDelete = "true"), exchange = @Exchange(value = FanoutConstants.ORDER_CREATE.EXCHANGE, type = ExchangeTypes.FANOUT, durable = "false", autoDelete = "true")))
	public void sms(String jsonStrMsg, Channel channel, Message message) {
		ConsumerUtils.handleByConsumer(jsonStrMsg, channel, message, new Consumer<Map<String, Object>>() {
			@Override
			public void accept(Map<String, Object> params) {
				log.info("sms params:{}", JsonUtil.toJsonString(params));
			}
		});
	}
	
	@RabbitListener(bindings = @QueueBinding(value = @Queue(value = FanoutConstants.ORDER_CREATE.COUNTMONEY_QUEUE, durable = "false", autoDelete = "true"), exchange = @Exchange(value = FanoutConstants.ORDER_CREATE.EXCHANGE, type = ExchangeTypes.FANOUT, durable = "false", autoDelete = "true")))
	public void countmoney(String jsonStrMsg, Channel channel, Message message) {
		ConsumerUtils.handleByConsumer(jsonStrMsg, channel, message, new Consumer<Map<String, Object>>() {
			@Override
			public void accept(Map<String, Object> params) {
				log.info("countmoney params:{}", JsonUtil.toJsonString(params));
			}
		});
	}
}