package com.company.gateway.developer.policy.impl;

import com.company.gateway.developer.policy.ServicePriorityPolicy;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.lang.NonNull;

public class DefaultServicePriorityPolicy implements ServicePriorityPolicy {
    public DefaultServicePriorityPolicy() {
    }

    public boolean support(@NonNull ServiceInstance serviceInstance) {
        return true;
    }

    public int serverOrder(@NonNull ServiceInstance serviceInstance) {
        return 10000;
    }

    public int getOrder() {
        return Integer.MAX_VALUE;
    }
}
