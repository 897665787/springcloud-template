package com.company.gateway.developer.policy.impl;

import com.company.gateway.developer.policy.ServicePriorityPolicy;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.lang.NonNull;

import java.util.Arrays;

public class OnLineServicePriorityPolicy implements ServicePriorityPolicy {
    private static final String TAG_ONLINE = "ONLINE";

    public OnLineServicePriorityPolicy() {
    }

    public boolean support(@NonNull ServiceInstance serviceInstance, String contextDeveloper) {
        String developRouteTags = serviceInstance.getMetadata().get("developer_route_tag");
        if (StringUtils.isBlank(developRouteTags)) {
            return false;
        }
        boolean online = Arrays.stream(developRouteTags.split(",")).anyMatch(str -> StringUtils.equalsIgnoreCase(str, TAG_ONLINE));
        return online;
    }

    public int serverOrder(@NonNull ServiceInstance serviceInstance) {
        return 2000;
    }

    public int getOrder() {
        return 200;
    }
}
