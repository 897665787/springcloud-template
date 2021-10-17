package com.company.framework.amqp.rabbit;

import java.util.HashMap;
import java.util.Map;

import lombok.Data;

@Data
public class MessageContent {
    private String beanName;
    private Map<String,Object> params = new HashMap<>();
    /**
     * 交换机名称
     */
    private String exchange;
    /**
     * 路由key
     */
    private String routeKey;
}
