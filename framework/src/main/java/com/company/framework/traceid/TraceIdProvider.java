
package com.company.framework.traceid;

public interface TraceIdProvider {
    /**
     * 生产TraceId
     *
     * @return traceId
     */
    String generateTraceId();
}
