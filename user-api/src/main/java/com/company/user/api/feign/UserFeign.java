package com.company.user.api.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.company.common.annotation.Idempotent;
import com.company.common.api.Result;
import com.company.user.api.feign.fallback.UserFeignFallback;
import com.company.user.api.request.UserReq;
import com.company.user.api.response.UserResp;

@FeignClient(value = "template-user", path = "/user", fallbackFactory = UserFeignFallback.class)
public interface UserFeign {

	@RequestMapping("/getById")
	Result<UserResp> getById(@RequestParam("id") Long id);

	@RequestMapping("/retryGet")
	Result<UserResp> retryGet(@RequestParam("id") Long id);

	@RequestMapping("/retryPost")
	Result<UserResp> retryPost(@RequestBody UserReq userReq);

	@RequestMapping("/idempotent")
	@Idempotent
	Result<UserResp> idempotent(@RequestBody UserReq userReq);

	@RequestMapping("/noreturn")
	@Idempotent
	Result<Void> noreturn();
}
