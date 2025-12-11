package com.company.framework.cache;

import com.company.framework.cache.guava.GuavaCache;
import com.company.framework.cache.redis.RedisCache;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.time.Duration;

@EnableCaching
@Configuration
public class CacheAutoConfiguration {

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

    @Bean
    public CacheManager cacheManager() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager();
        cacheManager.setCaffeine(Caffeine.newBuilder().maximumSize(500).expireAfterWrite(Duration.ofMinutes(10)));
        return cacheManager;
    }
}
