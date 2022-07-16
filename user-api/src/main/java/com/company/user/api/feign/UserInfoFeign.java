package com.company.user.api.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.company.common.api.Result;
import com.company.user.api.feign.fallback.UserInfoFeignFallback;
import com.company.user.api.request.UserInfoReq;
import com.company.user.api.response.UserInfoResp;

@FeignClient(value = "template-user", path = "/userinfo", fallbackFactory = UserInfoFeignFallback.class)
public interface UserInfoFeign {

	@RequestMapping("/findOrCreateUser")
	Result<UserInfoResp> findOrCreateUser(@RequestBody UserInfoReq userInfoReq);

}
