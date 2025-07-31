package com.company.job.gracefulshutdown;

import com.company.framework.deploy.ConsumerComponent;
import com.xxl.job.core.executor.impl.XxlJobSpringExecutor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * job执行器 下线
 *
 * @author JQ棣
 */
@Slf4j
@Component
public class XxlJobConsumerComponent implements ConsumerComponent {

    @Autowired
    private XxlJobSpringExecutor xxlJobExecutor;

    @Override
    public void offline() {
        // 下线job执行器
        xxlJobExecutor.destroy();
        log.info("job执行器已下线");
    }
}
