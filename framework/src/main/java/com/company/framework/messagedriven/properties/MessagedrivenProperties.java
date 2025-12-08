package com.company.framework.messagedriven.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@Data
@ConfigurationProperties(prefix = "messagedriven")
public class MessagedrivenProperties {

    private String prefix;

    private Exchange exchange;// 对应rabbitmq的exchange，对应rocketmq的topic
    private Queue queue;// 对应rabbitmq的queue，对应rocketmq的consumerGroup

    @Data
    public static class Exchange {
        private String direct;// 简单消息
        private String xdelayed; // 延迟消息
    }

    @Data
    public static class Queue {
        private NameKey xdelayed; // 延迟队列
        private NameKey deadLetter; // 死信队列
        private NameKey common; // 通用队列

        @Data
        public static class NameKey {
            private String name; // 队列名
            private String key; // 对应rabbitmq的routingKey，对应rocketmq的tag
        }
    }

}
