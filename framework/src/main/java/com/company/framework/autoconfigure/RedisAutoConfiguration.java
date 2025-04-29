package com.company.framework.autoconfigure;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.AllNestedConditions;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@Conditional(RedisAutoConfiguration.RedisCondition.class)
public class RedisAutoConfiguration {

	public static class RedisCondition extends AllNestedConditions {

		RedisCondition() {
			super(ConfigurationPhase.PARSE_CONFIGURATION);
		}

		@ConditionalOnProperty(prefix = "template.enable", name = "message-driven", havingValue = "redis")
		static class EnableProperty {
		}

		@ConditionalOnProperty(prefix = "spring.redis", name = "host")
		static class HostProperty {
		}

	}
}
