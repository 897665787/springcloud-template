package com.company.framework.deploy;

import com.company.common.util.JsonUtil;
import com.company.framework.context.SpringContextUtil;
import com.netflix.discovery.DiscoveryClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;
import org.springframework.cloud.loadbalancer.cache.DefaultLoadBalancerCacheManager;
import org.springframework.cloud.loadbalancer.core.CachingServiceInstanceListSupplier;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.List;

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
	public void refresh(String application) {
		// 从Eureka Server获取注册信息，默认true
		boolean fetchRegistry = SpringContextUtil.getBooleanProperty("eureka.client.fetch-registry", true);
		if (!fetchRegistry) {
			// 不获取注册信息，不需要刷新
			return;
		}

		this.refreshRegistry(application);
		this.refreshServerList(application);
	}

	/**
	 * 刷新注册列表
	 */
	private void refreshRegistry(String application) {
		try {
			DiscoveryClient client = SpringContextUtil.getBean(DiscoveryClient.class);
			log.info("{},application before:{}", application,
					JsonUtil.toJsonString(client.getApplication(application)));

			Method method = DiscoveryClient.class.getDeclaredMethod("refreshRegistry");
			method.setAccessible(true);
			method.invoke(client);
			method.setAccessible(false);
			log.info("{},application after:{}", application, JsonUtil.toJsonString(client.getApplication(application)));
			log.info("refresh registry success!!!");
		} catch (Exception e) {
			log.error("refresh registry fail!!!", e);
		}
	}

	/**
	 * 刷新服务列表
	 */
	private void refreshServerList(String application) {
		try {
			/**
			 * <pre>
			 * 服务列表缓存查找方法：
			 * 1.经过断点调试，发现服务下线后通过DiscoveryClient.getInstances是可以获取到剔除下线节点的服务列表的
			 * 2.在DiscoveryClient.getInstances中断点，根据栈信息，关注LoadBalance关键字的类，找到RoundRobinLoadBalancer.choose入口
			 * 3.通过feign调用不断调试，每次请求都到达RoundRobinLoadBalancer.choose，但不是每次请求都到达DiscoveryClient.getInstances，说明RoundRobinLoadBalancer.choose与DiscoveryClient.getInstances之间肯定存在一个缓存，即服务列表缓存
			 *
			 * 栈如下：
			 * RoundRobinLoadBalancer.choose
			 * RetryAwareServiceInstanceListSupplier.get
			 * CachingServiceInstanceListSupplier.CacheFlux.lookup 缓存在这里Cache
			 * CompositeDiscoveryClient.getInstances
			 * DiscoveryClient.getInstances
			 * </pre>
			 */
			DefaultLoadBalancerCacheManager cacheManager = SpringContextUtil
					.getBean(DefaultLoadBalancerCacheManager.class);
			Cache cache = cacheManager.getCache(CachingServiceInstanceListSupplier.SERVICE_INSTANCE_CACHE_NAME);

			log.info("{},cache before:{}", application, JsonUtil.toJsonString(cache.get(application, List.class)));
			cache.evict(application);
			log.info("{},cache after:{}", application, JsonUtil.toJsonString(cache.get(application, List.class)));
			log.info("refresh server list success!!!");
		} catch (Exception e) {
			log.error("refresh server list fail!!!", e);
		}
	}
}
