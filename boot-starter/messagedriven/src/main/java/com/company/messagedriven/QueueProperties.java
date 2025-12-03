package com.company.messagedriven;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@Data
@ConfigurationProperties(prefix = "messagedriven")
public class QueueProperties {

    private String prefix;
    private Exchange exchange;// 对应rabbitmq exchange，对应rocketmq topic
    private Queue queue;// 对应rabbitmq queue，对应rocketmq consumer group

    @Data
    public static class Exchange {
        private String direct;
        private String xdelayed;
    }

    @Data
    public static class Queue {
        private NameKey xdelayed;
        private NameKey dead_letter;
        private NameKey common;

        @Data
        public static class NameKey {
            private String name;
            private String key; // 对应rabbitmq routingKey，对应rocketmq tag
        }
    }

}
