package com.company.framework.gracefulshutdown;

/**
 * 消费型组件（比如注册中心、MQ、xxljob等会主动拉取数据的）
 *
 * @author JQ棣
 */
public interface ConsumerComponent {
    /**
     * 预停机
     */
    void preStop();
}
