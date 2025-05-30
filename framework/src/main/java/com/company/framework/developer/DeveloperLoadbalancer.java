package com.company.framework.developer;

import com.company.framework.developer.policy.ServicePriorityPolicyManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.*;
import org.springframework.cloud.loadbalancer.core.NoopServiceInstanceListSupplier;
import org.springframework.cloud.loadbalancer.core.RoundRobinLoadBalancer;
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;
import org.springframework.http.HttpHeaders;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

public class DeveloperLoadbalancer extends RoundRobinLoadBalancer {
    private final Logger logger = LoggerFactory.getLogger(DeveloperLoadbalancer.class);
    private final ObjectProvider<ServiceInstanceListSupplier> serviceInstanceListSupplierObjectProvider;
    private final ServicePriorityPolicyManager servicePriorityPolicyManager;

    @Value("${developer.header}")
    private String headerDeveloper;

    public DeveloperLoadbalancer(ObjectProvider<ServiceInstanceListSupplier> serviceInstanceListSupplierObjectProvider, String serviceId, ServicePriorityPolicyManager servicePriorityPolicyManager) {
        super(serviceInstanceListSupplierObjectProvider, serviceId);
        this.serviceInstanceListSupplierObjectProvider = serviceInstanceListSupplierObjectProvider;
        this.servicePriorityPolicyManager = servicePriorityPolicyManager;
    }

    public Mono<Response<ServiceInstance>> choose(Request request) {
        String contextDeveloper;
        if (request instanceof DefaultRequest) {
            RequestDataContext context = (RequestDataContext) request.getContext();
            contextDeveloper = Optional.ofNullable(context).map(RequestDataContext::getClientRequest).map(RequestData::getHeaders).map(v -> v.getFirst(headerDeveloper)).orElse(null);
        } else {
            contextDeveloper = null;
        }

        ServiceInstanceListSupplier supplier = serviceInstanceListSupplierObjectProvider.getIfAvailable(NoopServiceInstanceListSupplier::new);
        return supplier.get(request).next().map(allServerList -> {
            Map<Integer, List<ServiceInstance>> serverInfoMap = allServerList.stream().collect(Collectors.groupingBy(v -> servicePriorityPolicyManager.serverOrder(v, contextDeveloper)));
            Optional<Integer> minOrder = serverInfoMap.keySet().stream().min(Integer::compareTo);
            List<ServiceInstance> serviceInstances = minOrder.map(serverInfoMap::get).orElse(allServerList);
            ServiceInstance server = this.chooseServer(serviceInstances);
            return server != null ? new DefaultResponse(server) : new EmptyResponse();
        }).doOnError((e) -> {
            this.logger.error("Developer filter ServiceInstance error :: " + e.getMessage(), e);
        }).onErrorReturn(new EmptyResponse());
    }

    private ServiceInstance chooseServer(List<ServiceInstance> serverList) {
        if (serverList != null && !serverList.isEmpty()) {
            int size = serverList.size();
            return serverList.get(ThreadLocalRandom.current().nextInt(size));
        } else {
            return null;
        }
    }
}
