package com.company.gateway.trace.impl;

import com.company.gateway.trace.TraceIdProvider;

import java.util.UUID;

public class UUIDProvider implements TraceIdProvider {

    @Override
    public String generateTraceId() {
        return UUID.randomUUID().toString();
    }
}
