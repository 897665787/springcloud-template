package com.company.zuul.autoconfigure;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.AllNestedConditions;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Slf4j
@Configuration
@Conditional(RocketMQAutoConfiguration.RocketMQCondition.class)
@Import(org.apache.rocketmq.spring.autoconfigure.RocketMQAutoConfiguration.class)
public class RocketMQAutoConfiguration {

    public static class RocketMQCondition extends AllNestedConditions {

        RocketMQCondition() {
            super(ConfigurationPhase.PARSE_CONFIGURATION);
        }

        @ConditionalOnProperty(prefix = "template.enable", name = "message-queue", havingValue = "rocketmq")
        static class EnableProperty {
        }

        @ConditionalOnProperty(prefix = "rocketmq", name = "name-server")
        static class HostProperty {
        }

    }
}
