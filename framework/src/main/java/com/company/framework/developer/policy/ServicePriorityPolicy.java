package com.company.framework.developer.policy;

import org.springframework.cloud.client.ServiceInstance;
import org.springframework.core.Ordered;
import org.springframework.lang.NonNull;

public interface ServicePriorityPolicy extends Ordered {
    boolean support(@NonNull ServiceInstance serviceInstance);

    int serverOrder(@NonNull ServiceInstance serviceInstance);

    default int getOrder() {
        return 0;
    }
}
