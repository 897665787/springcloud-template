package com.company.framework.autoconfigure;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;

import com.company.framework.cache.CombinationCache;
import com.company.framework.cache.ICache;
import com.company.framework.cache.guava.GuavaCache;
import com.company.framework.cache.redis.RedisCache;

@Configuration
public class CacheAutoConfiguration {

	@Bean
	public StringRedisTemplate stringRedisTemplate(RedisConnectionFactory redisConnectionFactory) {
		StringRedisTemplate template = new StringRedisTemplate();
		template.setConnectionFactory(redisConnectionFactory);
		return template;
	}

	@Bean
	@ConditionalOnProperty(prefix = "template.enable", name = "cache", havingValue = "redis")
	public ICache redisCache(StringRedisTemplate stringRedisTemplate) {
		ICache redisCache = new RedisCache(stringRedisTemplate);
		return redisCache;
	}

	@Bean
	@ConditionalOnProperty(prefix = "template.enable", name = "cache", havingValue = "guava")
	public ICache guavaCache() {
		ICache guavaCache = new GuavaCache();
		return guavaCache;
	}

	@Bean
	@ConditionalOnProperty(prefix = "template.enable", name = "cache", havingValue = "combination")
	public ICache combinationCache(StringRedisTemplate stringRedisTemplate) {
		ICache redisCache = new RedisCache(stringRedisTemplate);
		ICache guavaCache = new GuavaCache();
		ICache combinationCache = new CombinationCache(redisCache, guavaCache);
		return combinationCache;
	}
}
