package com.company.user.api.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.company.user.api.constant.Constants;
import com.company.user.api.feign.fallback.DeviceFeignFallback;

@FeignClient(value = Constants.FEIGNCLIENT_VALUE, path = "/device", fallbackFactory = DeviceFeignFallback.class)
public interface DeviceFeign {

    /**
     * 判断设备是否已在线
     *
     * @param deviceid
     * @return
     */
    @RequestMapping("/isOnline")
    Boolean isOnline(@RequestParam("deviceid") String deviceid);
}
