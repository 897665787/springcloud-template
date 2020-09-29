package com.company.user.api.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.company.user.api.feign.fallback.UserFeignFallback;
import com.company.user.api.response.UserResp;

@FeignClient(value = "template-user", path = "/user", fallbackFactory = UserFeignFallback.class)
public interface UserFeign {

	@RequestMapping("/getById")
	UserResp getById(@RequestParam("id") Long id);
}
