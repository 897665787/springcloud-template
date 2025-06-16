package com.company.gateway.developer.policy.impl;

import com.company.gateway.developer.policy.ServicePriorityPolicy;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.lang.NonNull;

import java.util.List;
import java.util.Objects;

public class DeveloperSelfPriorityPolicy implements ServicePriorityPolicy {
    public DeveloperSelfPriorityPolicy() {
    }

    public boolean support(@NonNull ServiceInstance serviceInstance, List<String> developerList) {
        String developer = serviceInstance.getMetadata().get("developer");
        if (StringUtils.isBlank(developer)) {
            return false;
        }
        if (CollectionUtils.isEmpty(developerList)) {
            return false;
        }
        return developerList.contains(developer);
    }

    public int serverOrder(@NonNull ServiceInstance serviceInstance) {
        return 1000;
    }

    public int getOrder() {
        return 100;
    }
}
