package com.company.gateway.config.nacos;

import com.alibaba.cloud.nacos.NacosConfigManager;
import com.alibaba.cloud.nacos.NacosConfigProperties;
import com.alibaba.nacos.api.config.ConfigChangeEvent;
import com.alibaba.nacos.api.config.ConfigChangeItem;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.client.config.listener.impl.AbstractConfigChangeListener;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Slf4j
@Component
@ConditionalOnProperty(name = "spring.cloud.nacos.config.enabled", matchIfMissing = true)// 仅nacos启动时装配
@RequiredArgsConstructor
public class GlobalConfigChangeListener extends AbstractConfigChangeListener implements InitializingBean {

    private final NacosConfigManager nacosConfigManager;

    @Override
    public void receiveConfigChange(ConfigChangeEvent changeEvent) {
        Collection<ConfigChangeItem> changeItems = changeEvent.getChangeItems();
        for (ConfigChangeItem changeItem : changeItems) {
            log.info("changed:{} {} {} -> {}", changeItem.getType(), changeItem.getKey(), changeItem.getOldValue(), changeItem.getNewValue());
        }
        /* 这里无需发送事件，EnvironmentChangeListener会接收到事件，仅打印修改前后值日志
        Set<String> changedKeys = changeItems.stream().map(ConfigChangeItem::getKey).collect(Collectors.toSet());
        applicationContext.publishEvent(new EnvironmentChangeEvent(changedKeys));
         */
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        ConfigService configService = nacosConfigManager.getConfigService();
        NacosConfigProperties nacosConfigProperties = nacosConfigManager.getNacosConfigProperties();

        String dataId = nacosConfigProperties.getName();
        String group = nacosConfigProperties.getGroup();
        configService.addListener(dataId, group, this);
    }

}
