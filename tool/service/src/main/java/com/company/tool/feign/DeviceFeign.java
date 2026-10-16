package com.company.tool.feign;

import com.company.tool.feign.fallback.ThrowExceptionFallback;
import com.company.user.api.interfaces.DeviceApi;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(value = FeignConstants.USER, path = "/device", fallbackFactory = ThrowExceptionFallback.class)
public interface DeviceFeign extends DeviceApi {
}
