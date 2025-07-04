package com.company.gateway.autoconfigure;

import com.company.common.constant.HeaderConstants;
import com.company.gateway.trace.TraceManager;
import com.company.gateway.trace.TraceIdProvider;
import com.company.gateway.trace.impl.RandomProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TraceAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public TraceIdProvider traceIdProvider() {
        return new RandomProvider();
    }

    @Bean
    public TraceManager traceManager(TraceIdProvider traceIdProvider) {
        return new TraceManager(HeaderConstants.TRACE_ID, traceIdProvider);
    }
}
