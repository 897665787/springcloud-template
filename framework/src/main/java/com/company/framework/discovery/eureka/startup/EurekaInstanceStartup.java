package com.company.framework.discovery.eureka.startup;

import com.company.framework.context.SpringContextUtil;
import com.company.framework.gracefulshutdown.InstanceStartup;
import com.company.framework.messagedriven.MessageSender;
import com.company.framework.messagedriven.constants.BroadcastConstants;
import com.google.common.collect.Maps;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.netflix.eureka.serviceregistry.EurekaRegistration;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * eureka实例启动
 *
 * @author JQ棣
 */
@Slf4j
@Component
@ConditionalOnProperty(name = "eureka.client.enabled", havingValue = "true", matchIfMissing = true)
@RequiredArgsConstructor
public class EurekaInstanceStartup implements InstanceStartup {
    private final EurekaRegistration registration;

    private final MessageSender messageSender;

    @Override
    public void startup() {
        String application = SpringContextUtil.getProperty("spring.application.name");
        log.info("{} startup", application);

        Map<String, Object> params = Maps.newHashMap();
        params.put("type", "startup");
        params.put("application", application);
        params.put("ip", registration.getHost());
        params.put("port", registration.getPort());
        messageSender.sendBroadcastMessage(params, BroadcastConstants.DEPLOY.EXCHANGE);
    }

}
