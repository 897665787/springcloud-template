package com.company.framework.developer.policy;

import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.company.framework.developer.policy.impl.DefaultServicePriorityPolicy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.lang.NonNull;

public class ServicePriorityPolicyManager {
    private final List<ServicePriorityPolicy> servicePriorityPolicies;
    private ServicePriorityPolicy defaultServicePriorityPolicy;

    @Autowired
    public ServicePriorityPolicyManager(List<ServicePriorityPolicy> servicePriorityPolicies) {
        this.servicePriorityPolicies = servicePriorityPolicies.stream().sorted(Comparator.comparingInt(ServicePriorityPolicy::getOrder)).collect(Collectors.toList());
        this.defaultServicePriorityPolicy = new DefaultServicePriorityPolicy();
    }

    public void setDefaultServicePriorityPolicy(ServicePriorityPolicy defaultServicePriorityPolicy) {
        this.defaultServicePriorityPolicy = Optional.ofNullable(defaultServicePriorityPolicy).orElse(new DefaultServicePriorityPolicy());
    }

    public int serverOrder(@NonNull ServiceInstance serviceInstance, String contextDeveloper) {
        Iterator<ServicePriorityPolicy> var2 = this.servicePriorityPolicies.iterator();

        ServicePriorityPolicy priorityPolicy;
        do {
            if (!var2.hasNext()) {
                return this.defaultServicePriorityPolicy.serverOrder(serviceInstance);
            }

            priorityPolicy = var2.next();
        } while (!priorityPolicy.support(serviceInstance, contextDeveloper));

        return priorityPolicy.serverOrder(serviceInstance);
    }
}
