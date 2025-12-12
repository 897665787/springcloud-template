package com.company.framework.cache;

import com.company.framework.cache.guava.GuavaCache;
import com.company.framework.cache.redis.RedisCache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

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
    public CacheManager cacheManager(RedisConnectionFactory factory) {
        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofMinutes(10))   // 默认TTL
                .disableCachingNullValues()         // 不缓存null值
                .prefixCacheNameWith("cache:")      // key前缀
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()));
        return RedisCacheManager.builder(factory).cacheDefaults(config).build();
    }

    @Bean
    public CacheManager cacheManager() {
        Caffeine<Object, Object> caffeine = Caffeine.newBuilder()
                .expireAfterWrite(10, TimeUnit.MINUTES)  // 写入后10分钟过期
                .maximumSize(1000)                       // 最大缓存条目数
//                .recordStats()
                ;
        CaffeineCacheManager cacheManager = new CaffeineCacheManager();
        cacheManager.setCaffeine(caffeine);
        return cacheManager;
    }
}
