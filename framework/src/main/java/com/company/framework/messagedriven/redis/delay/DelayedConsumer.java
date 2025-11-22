package com.company.framework.messagedriven.redis.delay;

import lombok.Data;

@Data
public class DelayedConsumer {
    private String exchange;
    private String routingKey;
    private String messageJson;

    public DelayedConsumer(String exchange, String routingKey, String messageJson) {
        this.exchange = exchange;
        this.routingKey = routingKey;
        this.messageJson = messageJson;
    }
}
