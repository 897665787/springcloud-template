package com.company.framework.messagedriven.redis;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class MessageStreamKey {

    private final Map<String, Object> headers = new HashMap<>();

    private String jsonStrMsg;

    private String exchange;

    public void setHeader(String key, Object value) {
        this.headers.put(key, value);
    }
}