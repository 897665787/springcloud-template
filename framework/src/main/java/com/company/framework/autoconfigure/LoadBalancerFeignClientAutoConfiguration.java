package com.company.framework.autoconfigure;

import org.springframework.cloud.netflix.ribbon.SpringClientFactory;
import org.springframework.cloud.openfeign.ribbon.CachingSpringLoadBalancerFactory;
import org.springframework.cloud.openfeign.ribbon.LoadBalancerFeignClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.company.framework.aspect.FeignIPClient;

import feign.Client;

@Configuration
public class LoadBalancerFeignClientAutoConfiguration {

	/**
	 * 自定义feign.Client
	 * 
	 * @return
	 */
	@Bean
	public Client feignIPClient(CachingSpringLoadBalancerFactory cachingFactory, SpringClientFactory clientFactory) {
		return new LoadBalancerFeignClient(new FeignIPClient(), cachingFactory, clientFactory);
	}
}
