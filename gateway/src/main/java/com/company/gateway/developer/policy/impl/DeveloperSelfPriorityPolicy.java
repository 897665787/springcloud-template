package com.company.gateway.developer.policy.impl;

import com.company.gateway.developer.policy.ServicePriorityPolicy;
import com.company.gateway.developer.policy.context.DeveloperContext;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.lang.NonNull;

import java.util.Objects;

public class DeveloperSelfPriorityPolicy implements ServicePriorityPolicy {
    public DeveloperSelfPriorityPolicy() {
    }

    public boolean support(@NonNull ServiceInstance serviceInstance) {
        String developer = serviceInstance.getMetadata().get("developer");
        if (StringUtils.isBlank(developer)) {
            return false;
        }
        String contextDeveloper = DeveloperContext.get();
        if (StringUtils.isBlank(contextDeveloper)) {
            return false;
        }
        return Objects.equals(developer, contextDeveloper);
    }

    public int serverOrder(@NonNull ServiceInstance serviceInstance) {
        return 1000;
    }

    public int getOrder() {
        return 100;
    }
}
