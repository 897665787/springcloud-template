package com.company.gateway.developer.policy.impl;

import com.company.gateway.developer.policy.ServicePriorityPolicy;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.lang.NonNull;

import java.util.List;

public class DefaultServicePriorityPolicy implements ServicePriorityPolicy {
    public DefaultServicePriorityPolicy() {
    }

    public boolean support(@NonNull ServiceInstance serviceInstance, List<String> developerList) {
        return true;
    }

    public int serverOrder(@NonNull ServiceInstance serviceInstance) {
        return 10000;
    }

    public int getOrder() {
        return Integer.MAX_VALUE;
    }
}
