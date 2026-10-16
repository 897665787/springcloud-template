package com.company.adminapi.feign;

import com.company.adminapi.feign.fallback.ThrowExceptionFallback;
import com.company.tool.api.interfaces.AppVersionApi;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(value = FeignConstants.TOOL, path = "/appVersion", fallbackFactory = ThrowExceptionFallback.class)
public interface AppVersionFeign extends AppVersionApi {
}
