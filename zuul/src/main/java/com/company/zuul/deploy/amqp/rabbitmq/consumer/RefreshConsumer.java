package com.company.zuul.deploy.amqp.rabbitmq.consumer;

import com.company.zuul.amqp.constants.FanoutConstants;
import com.company.zuul.amqp.constants.HeaderConstants;
import com.company.zuul.amqp.rabbit.utils.ConsumerUtils;
import com.company.zuul.autoconfigure.RabbitAutoConfiguration;
import com.company.zuul.deploy.amqp.strategy.StrategyConstants;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.context.annotation.Conditional;
import org.springframework.stereotype.Component;

@Component
@Conditional(RabbitAutoConfiguration.RabbitCondition.class)
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