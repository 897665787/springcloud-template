package com.company.framework.autoconfigure;

import org.apache.commons.lang3.StringUtils;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.redisson.config.SingleServerConfig;
import org.springframework.boot.autoconfigure.condition.AllNestedConditions;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;

import com.company.framework.autoconfigure.RedissionAutoConfiguration.RedissonCondition;
import com.company.framework.redis.redisson.DistributeLockUtils;

@Configuration
@Conditional(RedissonCondition.class)
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
		RedissonClient redissonClient = Redisson.create(config);
		DistributeLockUtils.init(redissonClient);
		return redissonClient;
	}
	
	static class RedissonCondition extends AllNestedConditions {

		RedissonCondition() {
			super(ConfigurationPhase.PARSE_CONFIGURATION);
		}

		@ConditionalOnProperty(prefix = "template.enable", name = "redisson", havingValue = "true")
		static class EnableProperty {
		}

		@ConditionalOnProperty(prefix = "spring.redis", name = "host")
		static class HostProperty {
		}

	}
}