package com.company.framework.autoconfigure;

import com.company.common.constant.HeaderConstants;
import com.company.framework.trace.TraceIdProvider;
import com.company.framework.trace.TraceManager;
import com.company.framework.trace.provider.RandomProvider;
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
