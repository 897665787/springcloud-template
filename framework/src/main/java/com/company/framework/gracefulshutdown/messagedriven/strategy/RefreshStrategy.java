package com.company.framework.gracefulshutdown.messagedriven.strategy;

import com.company.framework.gracefulshutdown.ServerListRefresher;
import com.company.framework.messagedriven.BaseStrategy;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component(StrategyConstants.REFRESH_STRATEGY)
public class RefreshStrategy implements BaseStrategy<Map<String, Object>> {
    @Autowired(required = false)
    private ServerListRefresher serverListRefresher;

    @Override
    public void doStrategy(Map<String, Object> params) {
        if (serverListRefresher == null) {
            log.warn("This service is not connected to the registry, no need to refresh the service list");
            return;
        }
        String application = MapUtils.getString(params, "application");
        serverListRefresher.refresh(application);
        log.info("#### refresh success");
    }
}
