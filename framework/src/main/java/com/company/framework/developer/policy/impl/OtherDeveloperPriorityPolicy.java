package com.company.framework.developer.policy.impl;

import com.company.framework.context.HttpContextUtil;
import com.company.framework.developer.policy.ServicePriorityPolicy;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.lang.NonNull;

import java.util.Objects;

public class OtherDeveloperPriorityPolicy implements ServicePriorityPolicy {
    public OtherDeveloperPriorityPolicy() {
    }

    public boolean support(@NonNull ServiceInstance serviceInstance) {
        String developer = serviceInstance.getMetadata().get("developer");
        if (StringUtils.isBlank(developer)) {
            return false;
        }
        String userId = HttpContextUtil.currentUserId();
        if (StringUtils.isBlank(userId)) {
            return true;
        }
        return !Objects.equals(developer, userId);
    }

    public int serverOrder(@NonNull ServiceInstance serviceInstance) {
        return 20000;
    }

    public int getOrder() {
        return 300;
    }
}
