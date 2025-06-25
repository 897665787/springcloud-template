package com.company.user.api.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.company.common.annotation.Idempotent;
import com.company.user.api.constant.Constants;
import com.company.user.api.feign.fallback.UserFeignFallback;
import com.company.user.api.request.UserReq;
import com.company.user.api.response.UserResp;

@FeignClient(value = Constants.FEIGNCLIENT_VALUE, path = "/user", fallbackFactory = UserFeignFallback.class)
public interface UserFeign {

	@RequestMapping("/getById")
	UserResp getById(@RequestParam("id") Long id);

	@RequestMapping("/retryGet")
	UserResp retryGet(@RequestParam("id") Long id);

	@RequestMapping("/retryPost")
	UserResp retryPost(@RequestBody UserReq userReq);

	@RequestMapping("/idempotent")
	@Idempotent
	UserResp idempotent(@RequestBody UserReq userReq);

	@RequestMapping("/noreturn")
	@Idempotent
	Void noreturn();
}
