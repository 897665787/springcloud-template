package com.company.framework.gracefulshutdown.messagedriven.rabbitmq.consumer;

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

import com.company.framework.messagedriven.constants.FanoutConstants;
import com.company.framework.messagedriven.rabbitmq.utils.ConsumerUtils;
import com.company.framework.gracefulshutdown.messagedriven.strategy.StrategyConstants;
import com.rabbitmq.client.Channel;

@Component
@Conditional(RabbitMQAutoConfiguration.RabbitMQCondition.class)
public class RefreshConsumer {

	/**
	 * <pre>
	 * 临时队列名由fanout.deploy-${spring.application.name}-${spring.cloud.client.ip-address}-${server.port}构成
	 * 每个服务（包括同个服务的集群部署）都要有单独的队列来监听fanout exchange的消息（用于监听其他服务下线或上线事件，重新拉取注册信息）
	 * </pre>
	 *
	 * @param msg
	 */
	@RabbitListener(bindings = @QueueBinding(value = @Queue(value = FanoutConstants.DEPLOY.EXCHANGE
			+ "-${spring.application.name}-${spring.cloud.client.ip-address}-${server.port}", durable = "false", autoDelete = "true", exclusive = "true"), exchange = @Exchange(value = FanoutConstants.DEPLOY.EXCHANGE, type = ExchangeTypes.FANOUT, durable = "false", autoDelete = "true")))
	public void refresh(String jsonStrMsg, Channel channel, Message message) {
		message.getMessageProperties().setHeader(HeaderConstants.HEADER_STRATEGY_NAME,
				StrategyConstants.REFRESH_STRATEGY);
		ConsumerUtils.handleByStrategy(jsonStrMsg, channel, message);
	}
}
