package com.company.web.rabbitmq.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;

import com.company.framework.autoconfigure.RabbitAutoConfiguration.RabbitCondition;
import com.company.web.rabbitmq.QueueConstant;

/**
 * 延时队列(用2个队列实现)
 * 
 * @return
 */
@Configuration
@Conditional(RabbitCondition.class)
public class DelayConfig {

	@Bean
	public DirectExchange directExchange() {
		return new DirectExchange(QueueConstant.EXCHANGE);
	}

	/**
	 * 死信队列
	 * 
	 * @return
	 */
	@Bean
	public Queue delayQueue() {
		return QueueBuilder.durable(QueueConstant.DELAY_QUEUE.DEAD_QUEUE)
				// 最大延迟毫秒数（这里指定1天）
				.withArgument("x-message-ttl", 86400000)
				// 配置到期后转发的交换
				.withArgument("x-dead-letter-exchange", QueueConstant.EXCHANGE)
				// 配置到期后转发的路由键
				.withArgument("x-dead-letter-routing-key", QueueConstant.COMMON_QUEUE.ROUTING_KEY)// ttl到期后转发到普通公共队列
				.build();
	}

	/**
	 * 死信队列和交换机绑定
	 * 
	 * @return
	 */
	@Bean
	public Binding delayBinding(Queue delayQueue, DirectExchange directExchange) {
		return BindingBuilder.bind(delayQueue).to(directExchange).with(QueueConstant.DELAY_QUEUE.ROUTING_KEY);
	}
}