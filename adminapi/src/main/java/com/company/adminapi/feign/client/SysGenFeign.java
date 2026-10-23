package com.company.adminapi.feign.client;

/**
 * 代码生成服务 Feign
 *
 * @author JQ棣
 * @email 897665787@qq.com
 * @date 2026/10/22 10:30
 * @Copyright Copyright (c) Speediance Inc. All Rights Reserved.
 **/

import org.springframework.cloud.openfeign.FeignClient;

import com.company.adminapi.feign.FeignConstants;
import com.company.adminapi.feign.fallback.ThrowExceptionFallback;
import com.company.system.api.interfaces.SysGenApi;

@FeignClient(value = FeignConstants.CODE_GENERATE, path = "/sysGen", fallbackFactory = ThrowExceptionFallback.class)
public interface SysGenFeign extends SysGenApi {
}
