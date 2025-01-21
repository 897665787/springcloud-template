package com.company.framework.loadbalance;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.cloud.client.ConditionalOnDiscoveryEnabled;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.loadbalancer.core.ReactorLoadBalancer;
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;
import org.springframework.cloud.loadbalancer.support.LoadBalancerClientFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;

@Order(-2147482649)
//@Configuration
@ConditionalOnDiscoveryEnabled
public class DeveloperLoadBalancerConfiguration {
	public static final int DYNAMIC_ROUTE_ORDER = -2147482648;

	public DeveloperLoadBalancerConfiguration() {
	}

	@Bean
	@ConditionalOnMissingBean
	public ReactorLoadBalancer<ServiceInstance> developerServiceInstanceLoadBalancer(
			Environment environment, LoadBalancerClientFactory loadBalancerClientFactory
//			, ServicePriorityPolicyManager servicePriorityPolicyManager
	) {
		String name = environment.getProperty("loadbalancer.client.name");
		return new DeveloperLoadBalancer(loadBalancerClientFactory.getLazyProvider(name, ServiceInstanceListSupplier.class), name
//				, servicePriorityPolicyManager
		);
	}

//	@Bean
//	@ConditionalOnMissingBean({DeveloperSelfPriorityPolicy.class})
//	public ServicePriorityPolicy developerSelfPriorityPolicy() {
//		return new DeveloperSelfPriorityPolicy();
//	}
//
//	@Bean
//	@ConditionalOnMissingBean({OtherDeveloperPriorityPolicy.class})
//	public OtherDeveloperPriorityPolicy otherDeveloperPriorityPolicy() {
//		return new OtherDeveloperPriorityPolicy();
//	}
//
//	@Bean
//	@ConditionalOnMissingBean({OnLineServicePriorityPolicy.class})
//	public ServicePriorityPolicy onLineServicePriorityPolicy() {
//		return new OnLineServicePriorityPolicy();
//	}
//
//	@Bean
//	@ConditionalOnMissingBean({ServicePriorityPolicyManager.class})
//	public ServicePriorityPolicyManager servicePriorityPolicyManager(List<ServicePriorityPolicy> servicePriorityPolicies) {
//		return new ServicePriorityPolicyManager(servicePriorityPolicies);
//	}
}