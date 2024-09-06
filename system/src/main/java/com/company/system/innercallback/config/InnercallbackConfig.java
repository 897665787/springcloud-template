package com.company.system.innercallback.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "innercallback")
public class InnercallbackConfig {
    private Integer defaultIncreaseSeconds = 2;// 增量秒数，默认2s
    private Integer defaultMaxfailure = 10;// 最大失败次数，默认10次
}
