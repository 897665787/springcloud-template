package com.company.framework.feign;

import feign.Client;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.cloud.netflix.ribbon.SpringClientFactory;
import org.springframework.cloud.openfeign.ribbon.CachingSpringLoadBalancerFactory;
import org.springframework.cloud.openfeign.ribbon.LoadBalancerFeignClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * copy from org.springframework.cloud.openfeign.ribbon.DefaultFeignLoadBalancedConfiguration
 * @author Spencer Gibb
 */
@Configuration(proxyBeanMethods = false)
class DefaultFeignLoadBalancedConfiguration {

	@Bean
	@ConditionalOnMissingBean
	public Client feignClient(CachingSpringLoadBalancerFactory cachingFactory,
							  SpringClientFactory clientFactory) {
//		return new LoadBalancerFeignClient(new Client.Default(null, null), cachingFactory,
//				clientFactory);
		// 自定义feign.Client
		return new LoadBalancerFeignClient(new FeignLoggerClient(null, null), cachingFactory, clientFactory);
	}

}