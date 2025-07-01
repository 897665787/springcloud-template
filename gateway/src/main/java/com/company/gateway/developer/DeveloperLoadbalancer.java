package com.company.gateway.developer;

import com.company.gateway.developer.policy.ServicePriorityPolicyManager;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.*;
import org.springframework.cloud.loadbalancer.core.NoopServiceInstanceListSupplier;
import org.springframework.cloud.loadbalancer.core.RoundRobinLoadBalancer;
import org.springframework.cloud.loadbalancer.core.SelectedInstanceCallback;
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;
import org.springframework.http.HttpHeaders;
import reactor.core.publisher.Mono;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * 开发者负载均衡器，在RoundRobinLoadBalancer基础上添加了根据请求头过滤服务列表的逻辑
 */
@Slf4j
public class DeveloperLoadbalancer extends RoundRobinLoadBalancer {
    private final Logger logger = LoggerFactory.getLogger(DeveloperLoadbalancer.class);
    private final ObjectProvider<ServiceInstanceListSupplier> serviceInstanceListSupplierProvider;
    private final ServicePriorityPolicyManager servicePriorityPolicyManager;
    private final String developerHeaders;
    private final AtomicInteger position;
    private final String serviceId;

    public DeveloperLoadbalancer(ObjectProvider<ServiceInstanceListSupplier> serviceInstanceListSupplierProvider, String serviceId, ServicePriorityPolicyManager servicePriorityPolicyManager, String developerHeaders) {
        super(serviceInstanceListSupplierProvider, serviceId);
        this.serviceId = serviceId;
        this.serviceInstanceListSupplierProvider = serviceInstanceListSupplierProvider;
        this.position = new AtomicInteger(new Random().nextInt(1000));
        this.servicePriorityPolicyManager = servicePriorityPolicyManager;
        this.developerHeaders = developerHeaders;
    }

    @Override
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

        ServiceInstanceListSupplier supplier = serviceInstanceListSupplierProvider.getIfAvailable(NoopServiceInstanceListSupplier::new);
        return supplier.get(request).next().map(allServerList -> {
            Map<Integer, List<ServiceInstance>> serverInfoMap = allServerList.stream().collect(Collectors.groupingBy(v -> servicePriorityPolicyManager.serverOrder(v, developerList)));
            Optional<Integer> minOrder = serverInfoMap.keySet().stream().min(Integer::compareTo);
            List<ServiceInstance> serviceInstances = minOrder.map(serverInfoMap::get).orElse(allServerList);

            return processInstanceResponse(supplier, serviceInstances);
        }).doOnError((e) -> {
            this.logger.error("Developer filter ServiceInstance error :: " + e.getMessage(), e);
        }).onErrorReturn(new EmptyResponse());
    }

    /**
     * copy from RoundRobinLoadBalancer
     *
     * @param supplier
     * @param serviceInstances
     * @return
     */
    private Response<ServiceInstance> processInstanceResponse(ServiceInstanceListSupplier supplier,
                                                              List<ServiceInstance> serviceInstances) {
        Response<ServiceInstance> serviceInstanceResponse = getInstanceResponse(serviceInstances);
        if (supplier instanceof SelectedInstanceCallback && serviceInstanceResponse.hasServer()) {
            ((SelectedInstanceCallback) supplier).selectedServiceInstance(serviceInstanceResponse.getServer());
        }
        return serviceInstanceResponse;
    }

    /**
     * copy from RoundRobinLoadBalancer
     *
     * @param instances
     * @return
     */
    private Response<ServiceInstance> getInstanceResponse(List<ServiceInstance> instances) {
        if (instances.isEmpty()) {
            if (log.isWarnEnabled()) {
                log.warn("No servers available for service: " + serviceId);
            }
            return new EmptyResponse();
        }

        // Do not move position when there is only 1 instance, especially some suppliers
        // have already filtered instances
        if (instances.size() == 1) {
            return new DefaultResponse(instances.get(0));
        }

        // Ignore the sign bit, this allows pos to loop sequentially from 0 to
        // Integer.MAX_VALUE
        int pos = this.position.incrementAndGet() & Integer.MAX_VALUE;

        ServiceInstance instance = instances.get(pos % instances.size());

        return new DefaultResponse(instance);
    }
}
