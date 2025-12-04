package com.company.framework.messagedriven.properties;

import lombok.Data;

@Data
public class Messagedriven {

    private Exchange exchange;// 对应rabbitmq的exchange，对应rocketmq的topic
    private Queue queue;// 对应rabbitmq的queue，对应rocketmq的consumerGroup

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
            private String key; // 对应rabbitmq的routingKey，对应rocketmq的tag
        }
    }

}
