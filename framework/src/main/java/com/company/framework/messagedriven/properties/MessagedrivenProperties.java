package com.company.framework.messagedriven.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@Data
@ConfigurationProperties(prefix = "messagedriven")
public class MessagedrivenProperties {

    private String prefix;
//    private Messagedriven framework;

    private Messagedriven.Exchange exchange;// 对应rabbitmq的exchange，对应rocketmq的topic
    private Messagedriven.Queue queue;// 对应rabbitmq的queue，对应rocketmq的consumerGroup

    @Data
    public static class Exchange {
        private String direct;
        private String xdelayed;
    }

    @Data
    public static class Queue {
        private Messagedriven.Queue.NameKey xdelayed;
        private Messagedriven.Queue.NameKey dead_letter;
        private Messagedriven.Queue.NameKey common;

        @Data
        public static class NameKey {
            private String name;
            private String key; // 对应rabbitmq的routingKey，对应rocketmq的tag
        }
    }

}
