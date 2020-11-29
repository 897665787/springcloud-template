//package com.company.framework.autoconfigure;
//
//import org.apache.commons.lang3.StringUtils;
//import org.redisson.Redisson;
//import org.redisson.api.RedissonClient;
//import org.redisson.config.Config;
//import org.redisson.config.SingleServerConfig;
//import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
//import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//@Configuration
//@ConditionalOnProperty(prefix = "spring", name = "redis")
//public class RedissionAutoConfiguration {
//	
//	@Bean
//	RedissonClient redissonSingle(RedisProperties redisProperties) {
//		Config config = new Config();
//		String address = "redis://" + redisProperties.getHost() + ":" + redisProperties.getPort();
//		SingleServerConfig serverConfig = config.useSingleServer().setAddress(address)
//				// .setTimeout(redssionProperties.getTimeout())
//				.setConnectionPoolSize(5).setConnectionMinimumIdleSize(1);
//		if (StringUtils.isNotBlank(redisProperties.getPassword())) {
//			serverConfig.setPassword(redisProperties.getPassword());
//		}
//		return Redisson.create(config);
//	}
//}