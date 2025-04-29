package com.company.web.messagedriven.redis.config;

import com.company.framework.autoconfigure.RabbitMQAutoConfiguration;
import com.company.web.messagedriven.Constants;
import com.company.web.messagedriven.redis.consumer.CommonConsumer;
import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

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
public class ConsumerConfig {

	private CommonConsumer commonConsumer;

	@Bean
	RedisMessageListenerContainer container(RedisConnectionFactory connectionFactory) {
		RedisMessageListenerContainer container = new RedisMessageListenerContainer();
		container.setConnectionFactory(connectionFactory);
		container.addMessageListener(commonConsumer, new PatternTopic("channelName"));
		return container;
	}

	@Bean
	public RedisMessageListenerContainer container(RedisConnectionFactory connectionFactory,
												   MessageListenerAdapter listenerAdapter) {
		RedisMessageListenerContainer container = new RedisMessageListenerContainer();
		container.setConnectionFactory(connectionFactory);
		container.addMessageListener(listenerAdapter, new ChannelTopic("messageChannel"));
		return container;
	}

	@Bean
	public MessageListenerAdapter listenerAdapter(CommonConsumer commonConsumer) {
		return new MessageListenerAdapter(commonConsumer, "onMessage");
	}
}