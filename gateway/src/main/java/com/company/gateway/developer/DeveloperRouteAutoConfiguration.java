package com.company.gateway.developer;

import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClients;
import org.springframework.context.annotation.Configuration;

@Configuration
@LoadBalancerClients(defaultConfiguration = {DeveloperLoadBalancerConfiguration.class})
public class DeveloperRouteAutoConfiguration {
    public DeveloperRouteAutoConfiguration() {
    }
}
