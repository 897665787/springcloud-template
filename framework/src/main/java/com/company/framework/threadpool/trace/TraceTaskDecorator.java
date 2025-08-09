package com.company.framework.threadpool.trace;

import com.company.framework.trace.TraceManager;
import com.company.framework.trace.thread.TraceRunnable;
import org.springframework.core.task.TaskDecorator;

public class TraceTaskDecorator implements TaskDecorator {
    private TraceManager traceManager;

    public TraceTaskDecorator(TraceManager traceManager) {
        this.traceManager = traceManager;
    }

    @Override
    public Runnable decorate(Runnable runnable) {
        return new TraceRunnable(runnable, traceManager, traceManager.get());
    }
}
