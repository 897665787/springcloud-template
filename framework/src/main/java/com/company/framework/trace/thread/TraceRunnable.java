package com.company.framework.trace.thread;

import com.company.framework.context.HeaderContextUtil;
import com.company.framework.trace.TraceManager;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

public class TraceRunnable implements Runnable {
    private final Runnable target;
    private final TraceManager traceManager;
    @Getter
    private String traceId;
    private Map<String, String> headerMap;

    public TraceRunnable(Runnable target, TraceManager traceManager, String traceId) {
        this.target = target;
        this.traceManager = traceManager;
        this.traceId = traceId;
//        headerMap = HeaderContextUtil.headerMap();// 直接引用不知道会不会有问题
        headerMap = new HashMap<>();// 深克隆一份数据给子线程
        HeaderContextUtil.headerMap().entrySet().forEach(entry -> headerMap.put(entry.getKey(), entry.getValue()));
    }

    @Override
    public void run() {
        try {
            traceManager.put(traceId);
            HeaderContextUtil.setHeaderMap(headerMap);

            target.run();
        } finally {
            traceManager.remove();
            HeaderContextUtil.remove();
        }
    }
}
