package com.company.gateway.deploy;

import java.lang.reflect.Method;
import java.util.Set;

import org.springframework.cloud.netflix.ribbon.SpringClientFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

import com.company.gateway.context.SpringContextUtil;
import com.netflix.discovery.DiscoveryClient;
import com.netflix.loadbalancer.DynamicServerListLoadBalancer;

import lombok.extern.slf4j.Slf4j;

/**
 * 部署相关接口
 * 
 * @author JQ棣
 *
 */
@Slf4j
@Component
public class RefreshHandler {

	/**
	 * 刷新
	 */
	public void refresh() {
		this.refreshRegistry();
		this.refreshRibbon();
	}

	/**
	 * 刷新注册列表
	 */
	private void refreshRegistry() {
		try {
			// 从Eureka Server获取注册信息，默认true
			boolean fetchRegistry = SpringContextUtil.getBooleanProperty("eureka.client.fetch-registry", true);
			if (!fetchRegistry) {
				// 不获取注册信息，不需要刷新
				return;
			}

			Method method = DiscoveryClient.class.getDeclaredMethod("refreshRegistry");
			method.setAccessible(true);
			method.invoke(SpringContextUtil.getBean(DiscoveryClient.class));
			log.info("refresh registry success!!!");
		} catch (Exception e) {
			log.error("refresh registry fail!!!", e);
		}
	}

	/**
	 * 刷新Ribbon列表
	 */
	private void refreshRibbon() {
		try {
			// 更新ribbon 的缓存
			SpringClientFactory springClientFactory = SpringContextUtil.getBean("springClientFactory");
			Method getContext = SpringClientFactory.class.getDeclaredMethod("getContext", String.class);
			getContext.setAccessible(true);
			Set<String> contextNames = springClientFactory.getContextNames();

			for (String contextName : contextNames) {
				AnnotationConfigApplicationContext context = (AnnotationConfigApplicationContext) getContext
						.invoke(springClientFactory, contextName);
				Object ribbonLoadBalancer = context.getBean("ribbonLoadBalancer");
				Method updateListOfServers = DynamicServerListLoadBalancer.class
						.getDeclaredMethod("updateListOfServers");
				updateListOfServers.setAccessible(true);
				updateListOfServers.invoke(ribbonLoadBalancer);
				log.info("{} refresh success!!!", contextName);
			}
			log.info("refresh ribbon success!!!");
		} catch (Exception e) {
			log.error("refresh ribbon fail!!!", e);
		}
	}
}
