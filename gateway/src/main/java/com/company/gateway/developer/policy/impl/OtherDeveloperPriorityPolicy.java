package com.company.gateway.developer.policy.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.lang.NonNull;
import org.springframework.util.CollectionUtils;

import com.company.gateway.developer.policy.ServicePriorityPolicy;

public class OtherDeveloperPriorityPolicy implements ServicePriorityPolicy {
    public OtherDeveloperPriorityPolicy() {
    }

    public boolean support(@NonNull ServiceInstance serviceInstance, List<String> developerList) {
        String developer = serviceInstance.getMetadata().get("developer");
        if (StringUtils.isBlank(developer)) {
            return false;
        }
        if (CollectionUtils.isEmpty(developerList)) {
            return true;
        }
        return !developerList.contains(developer);
    }

    public int serverOrder(@NonNull ServiceInstance serviceInstance) {
        return 20000;
    }

    public int getOrder() {
        return 300;
    }
}
