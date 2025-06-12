package com.company.tool.api.feign;

import com.company.common.api.Result;
import com.company.tool.api.constant.Constants;
import com.company.tool.api.feign.fallback.AppVersionFeignFallback;
import com.company.tool.api.response.AppVersionCheckResp;
import com.company.tool.api.response.BannerResp;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(value = Constants.FEIGNCLIENT_VALUE, path = "/appVersion", fallbackFactory = AppVersionFeignFallback.class)
public interface AppVersionFeign {

    /**
     * 检查
     *
     * @param appCode
     * @param currentVersion
     * @return
     */
    @GetMapping("/check")
    Result<AppVersionCheckResp> check(@RequestParam("appCode") String appCode, @RequestParam("currentVersion") String currentVersion);
}
