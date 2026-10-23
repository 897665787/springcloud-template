package com.company.adminapi.feign.client;

/**
 * 定时任务日志 Feign
 *
 * @author JQ棣
 * @email 897665787@qq.com
 * @date 2026/10/22 10:30
 * @Copyright Copyright (c) Speediance Inc. All Rights Reserved.
 **/

import com.company.adminapi.feign.FeignConstants;
import com.company.adminapi.feign.fallback.ThrowExceptionFallback;
import com.company.system.api.interfaces.SysJobLogApi;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(value = FeignConstants.SYSTEM, path = "/sysJobLog", fallbackFactory = ThrowExceptionFallback.class)
public interface SysJobLogFeign extends SysJobLogApi {
}
