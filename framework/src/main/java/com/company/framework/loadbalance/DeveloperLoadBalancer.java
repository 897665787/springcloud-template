package com.company.framework.loadbalance;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.reactive.DefaultResponse;
import org.springframework.cloud.client.loadbalancer.reactive.EmptyResponse;
import org.springframework.cloud.client.loadbalancer.reactive.Request;
import org.springframework.cloud.client.loadbalancer.reactive.Response;
import org.springframework.cloud.loadbalancer.core.NoopServiceInstanceListSupplier;
import org.springframework.cloud.loadbalancer.core.RoundRobinLoadBalancer;
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Stream;

public class DeveloperLoadBalancer extends RoundRobinLoadBalancer {
	private final Logger logger = LoggerFactory.getLogger(DeveloperLoadBalancer.class);
	ObjectProvider<ServiceInstanceListSupplier> serviceInstanceListSupplierObjectProvider;
//	private final ServicePriorityPolicyManager servicePriorityPolicyManager;

	public DeveloperLoadBalancer(ObjectProvider<ServiceInstanceListSupplier> serviceInstanceListSupplierObjectProvider, String serviceId
//			, ServicePriorityPolicyManager servicePriorityPolicyManager
	) {
		super(serviceInstanceListSupplierObjectProvider, serviceId);
		this.serviceInstanceListSupplierObjectProvider = serviceInstanceListSupplierObjectProvider;
//		this.servicePriorityPolicyManager = servicePriorityPolicyManager;
	}

	public Mono<Response<ServiceInstance>> choose(Request request) {
		return ((ServiceInstanceListSupplier)this.serviceInstanceListSupplierObjectProvider.getIfAvailable(NoopServiceInstanceListSupplier::new))
				.get(/*request*/).next().map((allServerList) -> {
					Stream<ServiceInstance> var10000 = allServerList.stream();
//			ServicePriorityPolicyManager var10001 = this.servicePriorityPolicyManager;
//			var10001.getClass();

//					String developer = (String)serviceInstance.getMetadata().get("developer");
//					String userName = (String)Optional.ofNullable(DetailsHelper.getUserDetails()).map(User::getUsername).orElse((Object)null);

//			Map<Integer, List<ServiceInstance>> serverInfoMap = (Map)var10000.collect(Collectors.groupingBy(var10001::serverOrder));
//			Optional<Integer> minOrder = serverInfoMap.keySet().stream().min(Integer::compareTo);
//			serverInfoMap.getClass();
//			List<ServiceInstance> serviceInstances = minOrder.map(serverInfoMap::get).orElse(allServerList);
			List<ServiceInstance> serviceInstances = allServerList;
			ServiceInstance server = this.chooseServer(serviceInstances);
			return (server != null ? new DefaultResponse(server) : new EmptyResponse());
		}).doOnError((e) -> this.logger.error("Developer filter ServiceInstance error :: " + e.getMessage(), e)).onErrorReturn(new EmptyResponse());
	}

	private ServiceInstance chooseServer(List<ServiceInstance> serverList) {
		if (serverList != null && !serverList.isEmpty()) {
			int size = serverList.size();
			return (ServiceInstance)serverList.get(ThreadLocalRandom.current().nextInt(size));
		} else {
			return null;
		}
	}
}
