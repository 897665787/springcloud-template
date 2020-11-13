//package com.company.framework.deploy;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//import com.netflix.loadbalancer.ServerListUpdater;
//import com.netflix.niws.loadbalancer.EurekaNotificationServerListUpdater;
//
///**
// * 在生产环境实践中有问题：服务无法自动同步
// */
//@Configuration
//public class ConsumerRibbonClientConfig {
// 
//	/**
//	 * 服务下线时，客户端经常无法感知到。从而导致feign使用ribbon负载均衡的时候，在服务下线之后，ribbon中的server list来不及更新，仍会负载一部分流量到已下线的服务
//	 * <h4>持有EurekaEventListener实例，监听refreshRegistry->fetchRegistry->onCacheRefreshed->fireEvent</h4>
//	 * 
//	 * @return
//	 */
//    @Bean
//    public ServerListUpdater ribbonServerListUpdater() {
//        return new EurekaNotificationServerListUpdater();
//    }
// 
//}