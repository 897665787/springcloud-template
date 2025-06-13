
package com.company.framework.traceid;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;

public class TraceIdManager {

    private String key = null;
    private TraceIdProvider traceIdProvider = null;

    public TraceIdManager(String key, TraceIdProvider traceIdProvider) {
        this.key = key;
        this.traceIdProvider = traceIdProvider;
    }

    public void put() {
        MDC.put(key, traceIdProvider.generateTraceId());
    }

    public void put(String traceId) {
        if (StringUtils.isNotEmpty(traceId)) {
            MDC.put(key, traceId);
            return;
        }
        put();
    }

    public String get() {
        return MDC.get(key);
    }

    public void remove() {
        MDC.remove(key);
    }
}
