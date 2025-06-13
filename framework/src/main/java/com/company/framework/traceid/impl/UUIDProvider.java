package com.company.framework.traceid.impl;

import com.company.framework.traceid.TraceIdProvider;

import java.util.UUID;

public class UUIDProvider implements TraceIdProvider {

    @Override
    public String generateTraceId() {
        return UUID.randomUUID().toString();
    }
}
