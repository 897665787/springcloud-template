package com.company.gateway.developer;

import com.company.gateway.developer.policy.ServicePriorityPolicyManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.cloud.client.ConditionalOnDiscoveryEnabled;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.loadbalancer.core.ReactorLoadBalancer;
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;
import org.springframework.cloud.loadbalancer.support.LoadBalancerClientFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;

@Order(DeveloperLoadBalancerConfiguration.DYNAMIC_ROUTE_ORDER - 1)
@ConditionalOnDiscoveryEnabled
public class DeveloperLoadBalancerConfiguration {
    public static final int DYNAMIC_ROUTE_ORDER = -2147482648;

    @Bean
    @ConditionalOnMissingBean
    public ReactorLoadBalancer<ServiceInstance> developerServiceInstanceLoadBalancer(Environment environment, LoadBalancerClientFactory loadBalancerClientFactory, ServicePriorityPolicyManager servicePriorityPolicyManager
            , @Value("${developer.headers}") String developerHeaders) {
        String name = environment.getProperty(LoadBalancerClientFactory.PROPERTY_NAME);
        return new DeveloperLoadbalancer(loadBalancerClientFactory.getLazyProvider(name, ServiceInstanceListSupplier.class), name, servicePriorityPolicyManager, developerHeaders);
    }
}
