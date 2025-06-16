
package com.company.gateway.trace;

public interface TraceIdProvider {
    /**
     * 生产TraceId
     *
     * @return traceId
     */
    String generateTraceId();
}
