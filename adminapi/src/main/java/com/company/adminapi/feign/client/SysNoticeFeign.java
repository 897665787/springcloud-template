package com.company.adminapi.feign.client;

/**
 * 通知公告 Feign
 *
 * @author JQ棣
 * @email 897665787@qq.com
 * @date 2026/10/22 10:30
 * @Copyright Copyright (c) Speediance Inc. All Rights Reserved.
 **/

import com.company.adminapi.feign.FeignConstants;
import com.company.adminapi.feign.fallback.ThrowExceptionFallback;
import com.company.system.api.interfaces.SysNoticeApi;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(value = FeignConstants.SYSTEM, path = "/sysNotice", fallbackFactory = ThrowExceptionFallback.class)
public interface SysNoticeFeign extends SysNoticeApi {
}
