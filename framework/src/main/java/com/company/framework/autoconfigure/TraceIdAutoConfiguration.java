package com.company.framework.autoconfigure;

import com.company.framework.sequence.SequenceGenerator;
import com.company.framework.traceid.TraceIdManager;
import com.company.framework.traceid.TraceIdProvider;
import com.company.framework.traceid.impl.RandomProvider;
import com.company.framework.traceid.impl.SequenceProvider;
import com.company.framework.traceid.impl.UUIDProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TraceIdAutoConfiguration {
    public static final String UNIQUE_KEY = "trace-id";

    @Bean
    @ConditionalOnMissingBean
    public TraceIdProvider traceIdProvider() {
        return new RandomProvider();
    }

    // 如果要求traceId强唯一
    /*
    @Bean
    @ConditionalOnMissingBean
    public TraceIdProvider traceIdProvider(SequenceGenerator sequenceGenerator) {
        return new SequenceProvider(sequenceGenerator);
    }
     */

    @Bean
    public TraceIdManager mdcManager(TraceIdProvider traceIdProvider) {
        return new TraceIdManager(UNIQUE_KEY, traceIdProvider);
    }
}
