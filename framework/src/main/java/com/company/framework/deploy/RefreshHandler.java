package com.company.framework.deploy;

import java.lang.reflect.Method;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.ribbon.SpringClientFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

import com.company.framework.context.SpringContextUtil;
import com.netflix.discovery.DiscoveryClient;
import com.netflix.loadbalancer.DynamicServerListLoadBalancer;

import lombok.extern.slf4j.Slf4j;

/**
 * 部署相关接口
 * 
 * @author Candi
 *
 */
@Slf4j
@Component
public class RefreshHandler {

	@Autowired(required = false)
	private MQAutoRefresh mqAutoRefresh;

	/**
	 * 通知其他服务刷新注册服务列表
	 */
	public void notify2Refresh(String msg) {
		if (mqAutoRefresh == null) {
			log.info("mqAutoRefresh is null");
			return;
		}
		mqAutoRefresh.send(msg);
	}

	/**
	 * 刷新注册列表
	 */
	public boolean refreshRegistry() {
		try {
			Method method = DiscoveryClient.class.getDeclaredMethod("refreshRegistry");
			method.setAccessible(true);
			method.invoke(SpringContextUtil.getBean(DiscoveryClient.class));
			log.info("refresh registry success!!!");
			refreshRibbon();
			return true;
		} catch (Exception e) {
			log.error("refresh registry fail!!!", e);
			return false;
		}
	}

	/**
	 * 刷新Ribbon列表
	 */
	public void refreshRibbon() {
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
