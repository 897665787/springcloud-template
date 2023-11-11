package com.company.user.amqp.rabbitmq.consumer;

import java.util.Map;
import java.util.function.Consumer;

import org.apache.commons.collections4.MapUtils;
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
public class RefundApplyResultConsumer {

	@RabbitListener(bindings = @QueueBinding(value = @Queue(value = FanoutConstants.REFUND_APPLY_RESULT.MEMBER_REFUND_QUEUE, durable = "false", autoDelete = "true"), exchange = @Exchange(value = FanoutConstants.REFUND_APPLY_RESULT.EXCHANGE, type = ExchangeTypes.FANOUT, durable = "false", autoDelete = "true")))
	public void memberRefund(String jsonStrMsg, Channel channel, Message message) {
		ConsumerUtils.handleByConsumer(jsonStrMsg, channel, message, new Consumer<Map<String, Object>>() {
			@Override
			public void accept(Map<String, Object> params) {
				log.info("memberRefund params:{}", JsonUtil.toJsonString(params));

				String orderCode = MapUtils.getString(params, "orderCode");
				// 根据orderCode查询会员订单，查不到的情况下直接退出
				if (!false) {
					log.info("不是会员订单");
					return;
				}
				
				// 处理会员订单逻辑
				Boolean success = MapUtils.getBoolean(params, "success");
				String message = MapUtils.getString(params, "message");
				String refundOrderCode = MapUtils.getString(params, "refundOrderCode");
				String attach = MapUtils.getString(params, "attach");
			}
		});
	}

	@RabbitListener(bindings = @QueueBinding(value = @Queue(value = FanoutConstants.REFUND_APPLY_RESULT.GOODS_REFUND_QUEUE, durable = "false", autoDelete = "true"), exchange = @Exchange(value = FanoutConstants.REFUND_APPLY_RESULT.EXCHANGE, type = ExchangeTypes.FANOUT, durable = "false", autoDelete = "true")))
	public void goodsRefund(String jsonStrMsg, Channel channel, Message message) {
		ConsumerUtils.handleByConsumer(jsonStrMsg, channel, message, new Consumer<Map<String, Object>>() {
			@Override
			public void accept(Map<String, Object> params) {
				log.info("goodsRefund params:{}", JsonUtil.toJsonString(params));

				String orderCode = MapUtils.getString(params, "orderCode");
				// 根据orderCode查询商品订单，查不到的情况下直接退出
				if (!false) {
					log.info("不是商品订单");
					return;
				}
				
				// 处理商品订单逻辑
				Boolean success = MapUtils.getBoolean(params, "success");
				String message = MapUtils.getString(params, "message");
				String refundOrderCode = MapUtils.getString(params, "refundOrderCode");
				String attach = MapUtils.getString(params, "attach");
			}
		});
	}
}