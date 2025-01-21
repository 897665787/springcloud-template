package com.company.framework.loadbalance;

import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClients;

//@Configuration
@LoadBalancerClients(defaultConfiguration = {DeveloperLoadBalancerConfiguration.class})
//@EnableConfigurationProperties({CoreProperties.class})
//@EnableComponents(
//		type = {"org.xxx.gateway.filter.RequiredUserDetailsFilter"}
//)
public class DeveloperRouteAutoConfiguration {
	public DeveloperRouteAutoConfiguration() {
	}
}
