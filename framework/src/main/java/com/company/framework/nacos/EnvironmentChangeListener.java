package com.company.framework.nacos;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.context.environment.EnvironmentChangeEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(name = "spring.cloud.nacos.config.enabled", matchIfMissing = true)// 仅nacos启动时装配
public class EnvironmentChangeListener {

    @Autowired
    private SpringValueAutoRefreshProcessor springValueAutoRefreshProcessor;

    @EventListener
    public void environmentChange(EnvironmentChangeEvent event) {
        springValueAutoRefreshProcessor.changedKeys(event.getKeys());
    }
}
