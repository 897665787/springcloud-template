package com.company.framework.loadbalance;

import com.alibaba.fastjson.JSON;
import com.google.common.base.Optional;
import com.netflix.loadbalancer.ILoadBalancer;
import com.netflix.loadbalancer.Server;
import com.netflix.loadbalancer.ZoneAvoidanceRule;

import java.util.List;

public class DeveloperRule extends ZoneAvoidanceRule {
    @Override
    public Server choose(Object key) {
        System.out.println("key: " + key);
        ILoadBalancer lb = getLoadBalancer();
        List<Server> allServers = lb.getAllServers();
        System.out.println("allServers: " + JSON.toJSONString(allServers));
        Optional<Server> server = getPredicate().chooseRoundRobinAfterFiltering(lb.getAllServers(), key);
        if (server.isPresent()) {
            return server.get();
        }
        return super.choose(key);
    }
}