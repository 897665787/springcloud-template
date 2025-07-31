package com.company.framework.deploy;

/**
 * 服务列表刷新器
 *
 * @author JQ棣
 */
public interface ServerListRefresher {

    /**
     * 刷新
     */
    void refresh(String application);
}
