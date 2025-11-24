package com.company.framework.messagedriven.redis.delay;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DelayedConsumer {
    private String exchange;
    private String routingKey;
    private String messageJson;
}
