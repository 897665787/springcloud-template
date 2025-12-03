package com.company.messagedriven.rabbitmq.config;

import com.company.messagedriven.rabbitmq.RabbitMQAutoConfiguration;
import com.company.messagedriven.QueueProperties;
import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;

/**
 * 延时队列(用2个队列实现)
 *
 * <pre>
 * 无法解决高延时消息阻塞低延时消息问题
 * </pre>
 *
 * @return
 */
@Configuration
@Conditional(RabbitMQAutoConfiguration.RabbitMQCondition.class)
public class DelayConfig {

	@Bean
	public DirectExchange directExchange(QueueProperties queueProperties) {
        return new DirectExchange(queueProperties.getExchange().getDirect());
	}

	/**
	 * 死信队列
	 *
	 * @return
	 */
	@Bean
	public Queue delayQueue(QueueProperties queueProperties) {
		return QueueBuilder.durable(queueProperties.getQueue().getDead_letter().getName())
				// 最大延迟毫秒数（这里指定1天）
				.withArgument("x-message-ttl", 86400000)
				// 配置到期后转发的交换
				.withArgument("x-dead-letter-exchange", queueProperties.getExchange().getDirect())
				// 配置到期后转发的路由键
				.withArgument("x-dead-letter-routing-key", queueProperties.getQueue().getCommon().getKey())// ttl到期后转发到普通公共队列
				.build();
	}

	/**
	 * 死信队列和交换机绑定
	 *
	 * @return
	 */
	@Bean
	public Binding delayBinding(Queue delayQueue, DirectExchange directExchange, QueueProperties queueProperties) {
		return BindingBuilder.bind(delayQueue).to(directExchange).with(queueProperties.getQueue().getDead_letter().getKey());
	}
}
