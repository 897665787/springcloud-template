package com.company.framework.autoconfigure;

import org.springframework.boot.autoconfigure.condition.AllNestedConditions;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;

import com.company.framework.autoconfigure.RedisAutoConfiguration.RedisCondition;
import com.company.framework.redis.RedisUtils;

@Configuration
@Conditional(RedisCondition.class)
public class RedisAutoConfiguration {

	@Bean
	public StringRedisTemplate stringRedisTemplate(RedisConnectionFactory redisConnectionFactory) {
		StringRedisTemplate template = new StringRedisTemplate();
		template.setConnectionFactory(redisConnectionFactory);
		RedisUtils.init(template);
		return template;
	}

	static class RedisCondition extends AllNestedConditions {

		RedisCondition() {
			super(ConfigurationPhase.PARSE_CONFIGURATION);
		}

		@ConditionalOnProperty(prefix = "template.enable", name = "redis", havingValue = "true")
		static class EnableProperty {
		}

		@ConditionalOnProperty(prefix = "spring.redis", name = "host")
		static class HostProperty {
		}

	}
}
