package com.company.gateway.developer.policy.impl;

import com.company.gateway.developer.policy.ServicePriorityPolicy;
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
        // TODO 想办法从请求中获取路由信息
//        String userId = HttpContextUtil.currentUserId();
        String userId = null;
        if (StringUtils.isBlank(userId)) {
            return false;
        }
        return Objects.equals(developer, userId);
    }

    public int serverOrder(@NonNull ServiceInstance serviceInstance) {
        return 1000;
    }

    public int getOrder() {
        return 100;
    }
}
