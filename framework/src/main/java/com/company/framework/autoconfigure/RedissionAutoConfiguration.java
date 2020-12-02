package com.company.framework.autoconfigure;

import org.apache.commons.lang3.StringUtils;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.redisson.config.SingleServerConfig;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "spring.redis")
public class RedissionAutoConfiguration {

	@Bean(destroyMethod = "shutdown")
	public RedissonClient redisson(RedisProperties redisProperties) {
		Config config = new Config();
		String address = "redis://" + redisProperties.getHost() + ":" + redisProperties.getPort();

		SingleServerConfig serverConfig = config.useSingleServer().setAddress(address)
				.setConnectTimeout((int) redisProperties.getTimeout().toMillis())
				.setDatabase(redisProperties.getDatabase());
		if (StringUtils.isNotBlank(redisProperties.getPassword())) {
			serverConfig.setPassword(redisProperties.getPassword());
		}
		return Redisson.create(config);
	}
}