package com.company.framework.context.thread;

import com.company.framework.context.HeaderContextUtil;
import com.company.framework.trace.TraceManager;
import com.company.framework.trace.thread.TraceRunnable;

import java.util.HashMap;
import java.util.Map;

public class HeaderContextRunnable extends TraceRunnable {
    private Map<String, String> headerMap;

    public HeaderContextRunnable(Runnable target, TraceManager traceManager, String traceId) {
        super(target, traceManager, traceId);

//        headerMap = HeaderContextUtil.headerMap();// 直接引用不知道会不会有问题
        headerMap = new HashMap<>();// 深克隆一份数据给子线程
        HeaderContextUtil.headerMap().entrySet().forEach(entry -> headerMap.put(entry.getKey(), entry.getValue()));
    }

    @Override
    public void run() {
        try {
            HeaderContextUtil.setHeaderMap(headerMap);
            super.run();
        } finally {
            HeaderContextUtil.remove();
        }
    }
}
