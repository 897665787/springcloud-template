package com.company.framework.lock;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;

import com.company.framework.lock.impl.JvmLockClient;
import com.company.framework.lock.impl.RedissonLockClient;

@Configuration
public class LockAutoConfiguration {

	@Bean
	@ConditionalOnProperty(prefix = "template.enable", name = "lock", havingValue = "jvm")
	public LockClient jvmLockClient(StringRedisTemplate stringRedisTemplate) {
		LockClient lockClient = new JvmLockClient();
		return lockClient;
	}

	@Bean
	@ConditionalOnProperty(prefix = "template.enable", name = "lock", havingValue = "redisson")
	public LockClient redissonLockClient(RedisProperties redisProperties) {
		LockClient lockClient = new RedissonLockClient(redisProperties);
		return lockClient;
	}
}
