package com.company.framework.discovery.nacos.gracefulshutdown;

import com.alibaba.cloud.nacos.registry.NacosRegistration;
import com.alibaba.cloud.nacos.registry.NacosServiceRegistry;
import com.company.framework.context.SpringContextUtil;
import com.company.framework.gracefulshutdown.ConsumerComponent;
import com.company.framework.messagedriven.MessageSender;
import com.company.framework.messagedriven.constants.BroadcastConstants;
import com.google.common.collect.Maps;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * nacos服务下线
 *
 * @author JQ棣
 */
@Slf4j
@Component
@ConditionalOnProperty(name = "spring.cloud.nacos.discovery.enabled", havingValue = "true", matchIfMissing = true)
@RequiredArgsConstructor
public class NacosConsumerComponent implements ConsumerComponent {

    private final NacosServiceRegistry serviceRegistry;
    private final NacosRegistration registration;

    private final MessageSender messageSender;

    @Override
    public void preStop() {
        serviceRegistry.deregister(registration);

        // 通知其他服务刷新服务列表，即时中断请求流量
        Map<String, Object> params = Maps.newHashMap();
        params.put("type", "offline");
        String application = SpringContextUtil.getProperty("spring.application.name");
        params.put("application", application);
        params.put("ip", registration.getHost());
        params.put("port", registration.getPort());
        messageSender.sendBroadcastMessage(params, BroadcastConstants.DEPLOY.EXCHANGE);
        log.info("服务{}已在注册中心下线", application);
    }
}
