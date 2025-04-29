package com.company.web.messagedriven.redis.config;

import com.company.framework.autoconfigure.RabbitMQAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.stream.Consumer;
import org.springframework.data.redis.connection.stream.ObjectRecord;
import org.springframework.data.redis.connection.stream.ReadOffset;
import org.springframework.data.redis.connection.stream.StreamOffset;
import org.springframework.data.redis.stream.StreamListener;
import org.springframework.data.redis.stream.StreamMessageListenerContainer;
import org.springframework.data.redis.stream.Subscription;

import java.time.Duration;

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

	@Bean
	public StreamMessageListenerContainer<String, ObjectRecord<String, String>> streamMessageListenerContainer(
			RedisConnectionFactory redisConnectionFactory) {

		StreamMessageListenerContainer.StreamMessageListenerContainerOptions<String, ObjectRecord<String, String>> options =
				StreamMessageListenerContainer.StreamMessageListenerContainerOptions
						.builder()
						.pollTimeout(Duration.ofSeconds(1))
						.targetType(String.class)
						.build();

		return StreamMessageListenerContainer.create(redisConnectionFactory, options);
	}

	@Bean
	public Subscription subscription(
			StreamMessageListenerContainer<String, ObjectRecord<String, String>> container,
			StreamListener<String, ObjectRecord<String, String>> streamListener) {

		return container.receive(
				Consumer.from("group1", "consumer1"),
				StreamOffset.create("streamKey", ReadOffset.lastConsumed()),
				streamListener);
	}
}