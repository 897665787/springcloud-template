
package com.company.framework.trace;

public interface TraceIdProvider {
    /**
     * 生产TraceId
     *
     * @return traceId
     */
    String generateTraceId();
}
