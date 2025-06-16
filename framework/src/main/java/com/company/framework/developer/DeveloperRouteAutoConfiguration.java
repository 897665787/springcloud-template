package com.company.framework.developer;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClients;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(name = "developer.enabled", havingValue = "true")
@LoadBalancerClients(defaultConfiguration = {DeveloperLoadBalancerConfiguration.class})
public class DeveloperRouteAutoConfiguration {
}
