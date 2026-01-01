package com.company.gateway.config.nacos;

import lombok.RequiredArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.context.environment.EnvironmentChangeEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(name = "spring.cloud.nacos.config.enabled", matchIfMissing = true)// 仅nacos启动时装配
@RequiredArgsConstructor
public class EnvironmentChangeListener {

    private final SpringValueAutoRefreshProcessor springValueAutoRefreshProcessor;

    @EventListener
    public void environmentChange(EnvironmentChangeEvent event) {
        springValueAutoRefreshProcessor.changedKeys(event.getKeys());
    }
}
