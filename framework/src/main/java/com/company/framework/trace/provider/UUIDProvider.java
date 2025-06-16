package com.company.framework.trace.provider;

import com.company.framework.trace.TraceIdProvider;

import java.util.UUID;

public class UUIDProvider implements TraceIdProvider {

    @Override
    public String generateTraceId() {
        return UUID.randomUUID().toString();
    }
}
