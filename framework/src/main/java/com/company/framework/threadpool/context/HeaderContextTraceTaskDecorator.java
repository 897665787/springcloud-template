package com.company.framework.threadpool.context;

import com.company.framework.context.thread.HeaderContextRunnable;
import com.company.framework.trace.TraceManager;
import org.springframework.core.task.TaskDecorator;

public class HeaderContextTraceTaskDecorator implements TaskDecorator {
    private TraceManager traceManager;

    public HeaderContextTraceTaskDecorator(TraceManager traceManager) {
        this.traceManager = traceManager;
    }

    @Override
    public Runnable decorate(Runnable runnable) {
        return new HeaderContextRunnable(runnable, traceManager, traceManager.get());
    }
}
