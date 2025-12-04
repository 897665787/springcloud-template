package com.company.web.messagedriven;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@Data
@ConfigurationProperties(prefix = "messagedriven")
public class MessagedrivenProperties extends com.company.framework.messagedriven.MessagedrivenProperties {

    private String prefix;
    private Exchange exchange;// 对应rabbitmq的exchange，对应rocketmq的topic
    private Queue queue;// 对应rabbitmq的queue，对应rocketmq的consumerGroup


}
