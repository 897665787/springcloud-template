package com.company.user.api.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.company.common.api.Result;
import com.company.user.api.constant.Constants;
import com.company.user.api.feign.fallback.OpenAccessAccountFeignFallback;

@FeignClient(value = Constants.FEIGNCLIENT_VALUE, path = "/openAccessAccount", fallbackFactory = OpenAccessAccountFeignFallback.class)
public interface OpenAccessAccountFeign {

	@RequestMapping("/getAppKeyByAppid")
	Result<String> getAppKeyByAppid(@RequestParam("appid") String appid);
}
