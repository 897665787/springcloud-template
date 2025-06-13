package com.company.framework.traceid.impl;

import com.company.framework.sequence.SequenceGenerator;
import com.company.framework.traceid.TraceIdProvider;

public class SequenceProvider implements TraceIdProvider {

    private SequenceGenerator sequenceGenerator = null;

    public SequenceProvider(SequenceGenerator sequenceGenerator) {
        this.sequenceGenerator = sequenceGenerator;
    }

    @Override
    public String generateTraceId() {
        return sequenceGenerator.nextStrId();
    }
}