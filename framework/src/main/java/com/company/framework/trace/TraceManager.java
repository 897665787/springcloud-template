
package com.company.framework.trace;

import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;

import java.util.*;

public class TraceManager {

    private String key = null;
    private TraceIdProvider traceIdProvider = null;

    public TraceManager(String key, TraceIdProvider traceIdProvider) {
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

    public Map<String, List<String>> headers() {
        Map<String, List<String>> headers = Maps.newHashMap();
        headers.put(key, Collections.singletonList(get()));
        return headers;
    }
}
