package com.company.system.innercallback.processor;

/**
 * 放弃请求处理器
 *
 * @author JQ棣
 */
public interface AbandonRequestProcessor {
    /**
     * 放弃请求之后
     *
     * @return
     */
    void afterAbandonRequest(String jsonParams, String abandonReason);
}
