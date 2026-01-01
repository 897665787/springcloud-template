package com.company.job.gracefulshutdown;

import com.company.framework.gracefulshutdown.ConsumerComponent;
import com.xxl.job.core.executor.impl.XxlJobSpringExecutor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * job执行器 下线
 *
 * @author JQ棣
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class XxlJobConsumerComponent implements ConsumerComponent {

    private final XxlJobSpringExecutor xxlJobExecutor;

    @Override
    public void preStop() {
        // 下线job执行器
        xxlJobExecutor.destroy();
        log.info("job执行器已下线");
    }
}
