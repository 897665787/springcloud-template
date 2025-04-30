package com.company.web.messagedriven.redis.config;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.system.SystemUtil;
import com.company.framework.autoconfigure.RabbitMQAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisServerCommands;
import org.springframework.data.redis.connection.stream.Consumer;
import org.springframework.data.redis.connection.stream.ObjectRecord;
import org.springframework.data.redis.connection.stream.ReadOffset;
import org.springframework.data.redis.connection.stream.StreamOffset;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.stream.StreamListener;
import org.springframework.data.redis.stream.StreamMessageListenerContainer;
import org.springframework.data.redis.stream.Subscription;

import java.time.Duration;
import java.util.List;
import java.util.Properties;

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


	/**
	 * 创建 Redis Stream 集群消费的容器
	 * <p>
	 * Redis Stream 的 xreadgroup
	 * 命令：https://www.geek-book.com/src/docs/redis/redis/redis.io/commands/xreadgroup.html
	 */
	@Bean(initMethod = "start", destroyMethod = "stop")
	public StreamMessageListenerContainer<String, ObjectRecord<String, String>> redisStreamMessageListenerContainer(
			RedisMQTemplate redisMQTemplate, List<AbstractStreamMessageListener<?>> listeners) {
		RedisTemplate<String, ?> redisTemplate = redisMQTemplate.getRedisTemplate();
		checkRedisVersion(redisTemplate);
		// 第一步，创建 StreamMessageListenerContainer 容器
		// 创建 options 配置
		StreamMessageListenerContainer.StreamMessageListenerContainerOptions<String, ObjectRecord<String, String>> containerOptions = StreamMessageListenerContainer.StreamMessageListenerContainerOptions
				.builder().batchSize(10) // 一次性最多拉取多少条消息
				.targetType(String.class) // 目标类型。统一使用 String，通过自己封装的
				// AbstractStreamMessageListener 去反序列化
				.build();
		// 创建 container 对象
		StreamMessageListenerContainer<String, ObjectRecord<String, String>> container =
				// StreamMessageListenerContainer.create(redisTemplate.getRequiredConnectionFactory(),
				// containerOptions);
				DefaultStreamMessageListenerContainer
						.create(redisMQTemplate.getRedisTemplate().getRequiredConnectionFactory(), containerOptions);

		// 第二步，注册监听器，消费对应的 Stream 主题
		String consumerName = buildConsumerName();
		listeners.parallelStream().forEach(listener -> {
			// 创建 listener 对应的消费者分组
			try {
				redisTemplate.opsForStream().createGroup(listener.getStreamKey(), listener.getGroup());
			} catch (Exception ignore) {
			}
			// 设置 listener 对应的 redisTemplate
			listener.setRedisMQTemplate(redisMQTemplate);
			// 创建 Consumer 对象
			Consumer consumer = Consumer.from(listener.getGroup(), consumerName);
			// 设置 Consumer 消费进度，以最小消费进度为准
			StreamOffset<String> streamOffset = StreamOffset.create(listener.getStreamKey(), ReadOffset.lastConsumed());
			// 设置 Consumer 监听
			StreamMessageListenerContainer.StreamReadRequestBuilder<String> builder = StreamMessageListenerContainer.StreamReadRequest
					.builder(streamOffset).consumer(consumer).autoAcknowledge(false) // 不自动
					// ack
					.cancelOnError(throwable -> false); // 默认配置，发生异常就取消消费，显然不符合预期；因此，我们设置为
			// false
			container.register(builder.build(), listener);
		});
		return container;
	}

	/**
	 * 构建消费者名字，使用本地 IP + 进程编号的方式。 参考自 RocketMQ clientId 的实现
	 *
	 * @return 消费者名字
	 */
	private static String buildConsumerName() {
		return String.format("%s@%d", SystemUtil.getHostInfo().getAddress(), SystemUtil.getCurrentPID());
	}

	/**
	 * 校验 Redis 版本号，是否满足最低的版本号要求！
	 */
	private static void checkRedisVersion(RedisTemplate<String, ?> redisTemplate) {
		// 获得 Redis 版本
		Properties info = redisTemplate.execute((RedisCallback<Properties>) RedisServerCommands::info);
		String version = MapUtil.getStr(info, "redis_version");
		// 校验最低版本必须大于等于 5.0.0
		int majorVersion = Integer.parseInt(StrUtil.subBefore(version, '.', false));
		if (majorVersion < 5) {
			throw new IllegalStateException(StrUtil.format("您当前的 Redis 版本为 {}，小于最低要求的 5.0.0 版本！" + "请参考 {} 文档进行安装。",
					version));
		}
	}

}