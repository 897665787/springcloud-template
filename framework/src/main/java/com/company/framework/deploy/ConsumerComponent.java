package com.company.framework.deploy;

/**
 * 消费型组件（比如注册中心、MQ、xxljob等会消费数据的组件）
 *
 * @author JQ棣
 */
public interface ConsumerComponent {
    /**
     * 下线（用于实现优雅关闭）
     */
    void offline();
}
