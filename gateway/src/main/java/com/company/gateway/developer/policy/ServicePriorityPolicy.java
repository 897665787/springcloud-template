package com.company.gateway.developer.policy;

import org.springframework.cloud.client.ServiceInstance;
import org.springframework.core.Ordered;
import org.springframework.lang.NonNull;

public interface ServicePriorityPolicy extends Ordered {
    boolean support(@NonNull ServiceInstance serviceInstance, String contextDeveloper);

    int serverOrder(@NonNull ServiceInstance serviceInstance);

    default int getOrder() {
        return 0;
    }
}
