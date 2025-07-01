
package com.company.framework.deploy;

import com.company.framework.context.SpringContextUtil;
import com.company.framework.messagedriven.MessageSender;
import com.company.framework.messagedriven.constants.FanoutConstants;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.serviceregistry.Registration;
import org.springframework.cloud.client.serviceregistry.ServiceRegistry;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 注册中心服务 下线
 *
 * @author JQ棣
 */
@Slf4j
@Component
public class RegistryConsumerComponent implements ConsumerComponent {

    @Autowired(required = false)
    private ServiceRegistry serviceRegistry; // 注册中心：eureka | nacos
    @Autowired(required = false)
    private Registration registration; // 注册中心当前服务：eureka | nacos

    @Autowired
    private MessageSender messageSender;

    @Override
    public void offline() {
        // 下线注册中心
        if (serviceRegistry == null) {
            return;
        }
        serviceRegistry.deregister(registration);

        // 通知其他服务刷新服务列表，即时中断请求流量
        Map<String, Object> params = Maps.newHashMap();
        String application = SpringContextUtil.getProperty("spring.application.name");
        params.put("application", application);
        params.put("type", "offline");
        messageSender.sendFanoutMessage(params, FanoutConstants.DEPLOY.EXCHANGE);
        log.info("服务{}已在注册中心下线", application);
    }
}
