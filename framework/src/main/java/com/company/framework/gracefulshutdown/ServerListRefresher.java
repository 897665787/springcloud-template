package com.company.framework.gracefulshutdown;

/**
 * 服务列表刷新器
 *
 * @author JQ棣
 */
public interface ServerListRefresher {

    /**
     * 刷新
     */
    void refresh(String type, String application, String ip, int port);
}
