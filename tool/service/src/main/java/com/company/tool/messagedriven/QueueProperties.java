package com.company.tool.messagedriven;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "messagedriven")
public class QueueProperties {

    private String prefix;
    private Exchange exchange;
    private Queue queue;

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
            private String key;
        }
    }

}
