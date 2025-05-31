package com.company.framework.developer;

import com.company.framework.developer.policy.ServicePriorityPolicyManager;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.*;
import org.springframework.cloud.loadbalancer.core.NoopServiceInstanceListSupplier;
import org.springframework.cloud.loadbalancer.core.RoundRobinLoadBalancer;
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;
import org.springframework.http.HttpHeaders;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

public class DeveloperLoadbalancer extends RoundRobinLoadBalancer {
    private final Logger logger = LoggerFactory.getLogger(DeveloperLoadbalancer.class);
    private final ObjectProvider<ServiceInstanceListSupplier> serviceInstanceListSupplierObjectProvider;
    private final ServicePriorityPolicyManager servicePriorityPolicyManager;
    private final String developerHeaders;

    public DeveloperLoadbalancer(ObjectProvider<ServiceInstanceListSupplier> serviceInstanceListSupplierObjectProvider, String serviceId, ServicePriorityPolicyManager servicePriorityPolicyManager, String developerHeaders) {
        super(serviceInstanceListSupplierObjectProvider, serviceId);
        this.serviceInstanceListSupplierObjectProvider = serviceInstanceListSupplierObjectProvider;
        this.servicePriorityPolicyManager = servicePriorityPolicyManager;
        this.developerHeaders = developerHeaders;
    }

    public Mono<Response<ServiceInstance>> choose(Request request) {
        List<String> developerList;
        if (request instanceof DefaultRequest) {
            RequestDataContext context = (RequestDataContext) request.getContext();
            HttpHeaders httpHeaders = Optional.ofNullable(context).map(RequestDataContext::getClientRequest).map(RequestData::getHeaders).orElse(null);
            if (httpHeaders != null && StringUtils.isNotBlank(developerHeaders)) {
                developerList = Arrays.stream(developerHeaders.split(",")).filter(StringUtils::isNotBlank).map(httpHeaders::getFirst).collect(Collectors.toList());
            } else {
                developerList = null;
            }
        } else {
            developerList = null;
        }

        ServiceInstanceListSupplier supplier = serviceInstanceListSupplierObjectProvider.getIfAvailable(NoopServiceInstanceListSupplier::new);
        return supplier.get(request).next().map(allServerList -> {
            Map<Integer, List<ServiceInstance>> serverInfoMap = allServerList.stream().collect(Collectors.groupingBy(v -> servicePriorityPolicyManager.serverOrder(v, developerList)));
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
