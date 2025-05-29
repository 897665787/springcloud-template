package com.company.gateway.developer.policy.impl;

import com.company.gateway.developer.policy.ServicePriorityPolicy;
import com.company.gateway.developer.policy.filter.TokenContext;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.lang.NonNull;

import java.util.Objects;

public class OtherDeveloperPriorityPolicy implements ServicePriorityPolicy {
    public OtherDeveloperPriorityPolicy() {
    }

    public boolean support(@NonNull ServiceInstance serviceInstance) {
        String developer = serviceInstance.getMetadata().get("developer");// 入口层需配置token，不能配置userId
        if (StringUtils.isBlank(developer)) {
            return false;
        }
        String token = TokenContext.getToken();// gateway不会解析token，所以直接拿token与developer对比
        if (StringUtils.isBlank(token)) {
            return true;
        }
        return !Objects.equals(developer, token);
    }

    public int serverOrder(@NonNull ServiceInstance serviceInstance) {
        return 20000;
    }

    public int getOrder() {
        return 300;
    }
}
