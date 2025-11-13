package com.company.user.api.feign;

import com.company.common.api.Result;
import com.company.user.api.constant.Constants;
import com.company.user.api.feign.fallback.ThrowExceptionFallback;
import com.company.user.api.request.UserReq;
import com.company.user.api.response.UserResp;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = Constants.FEIGNCLIENT_VALUE, path = "/user", fallbackFactory = ThrowExceptionFallback.class)
public interface UserFeign {

	@RequestMapping("/getById")
	Result<UserResp> getById(@RequestParam("id") Long id);

	@RequestMapping("/retryGet")
	Result<UserResp> retryGet(@RequestParam("id") Long id);

	@RequestMapping("/retryPost")
	Result<UserResp> retryPost(@RequestBody UserReq userReq);

	@RequestMapping("/noreturn")
	Result<Void> noreturn();
}
