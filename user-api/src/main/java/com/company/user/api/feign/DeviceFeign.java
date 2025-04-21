package com.company.user.api.feign;

import com.company.common.api.Result;
import com.company.user.api.constant.Constants;
import com.company.user.api.feign.fallback.DeviceFeignFallback;
import com.company.user.api.feign.fallback.UserInfoFeignFallback;
import com.company.user.api.request.UserInfoReq;
import com.company.user.api.response.UserInfoResp;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Collection;
import java.util.Map;

@FeignClient(value = Constants.FEIGNCLIENT_VALUE, path = "/device", fallbackFactory = DeviceFeignFallback.class)
public interface DeviceFeign {

    /**
     * 判断设备是否已在线
     *
     * @param deviceid
     * @return
     */
    @RequestMapping("/isOnline")
    Result<Boolean> isOnline(@RequestParam("deviceid") String deviceid);
}
