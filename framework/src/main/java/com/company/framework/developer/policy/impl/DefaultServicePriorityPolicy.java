package com.company.framework.developer.policy.impl;

import com.company.framework.developer.policy.ServicePriorityPolicy;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.lang.NonNull;

public class DefaultServicePriorityPolicy implements ServicePriorityPolicy {
    public DefaultServicePriorityPolicy() {
    }

    public boolean support(@NonNull ServiceInstance serviceInstance, String contextDeveloper) {
        return true;
    }

    public int serverOrder(@NonNull ServiceInstance serviceInstance) {
        return 10000;
    }

    public int getOrder() {
        return Integer.MAX_VALUE;
    }
}
