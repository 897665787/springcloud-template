package com.company.framework.discovery.nacos.gracefulshutdown;

import com.alibaba.cloud.nacos.registry.NacosRegistration;
import com.alibaba.cloud.nacos.registry.NacosServiceRegistry;
import com.company.framework.context.SpringContextUtil;
import com.company.framework.gracefulshutdown.ConsumerComponent;
import com.company.framework.messagedriven.MessageSender;
import com.company.framework.messagedriven.constants.FanoutConstants;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
public class NacosConsumerComponent implements ConsumerComponent {

    @Autowired
    private NacosServiceRegistry serviceRegistry;
    @Autowired
    private NacosRegistration registration;

    @Autowired
    private MessageSender messageSender;

    @Override
    public void preStop() {
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
