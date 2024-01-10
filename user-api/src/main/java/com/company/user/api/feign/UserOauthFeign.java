package com.company.user.api.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.company.common.api.Result;
import com.company.user.api.constant.Constants;
import com.company.user.api.enums.UserOauthEnum;
import com.company.user.api.feign.fallback.UserOauthFeignFallback;
import com.company.user.api.request.UserOauthReq;
import com.company.user.api.response.UserOauthResp;

@FeignClient(value = Constants.FEIGNCLIENT_VALUE, path = "/useroauth", fallbackFactory = UserOauthFeignFallback.class)
public interface UserOauthFeign {

	@RequestMapping("/selectOauth")
	Result<UserOauthResp> selectOauth(@RequestParam("identityType") UserOauthEnum.IdentityType identityType,
			@RequestParam("identifier") String identifier);

	@RequestMapping("/selectIdentifier")
	Result<String> selectIdentifier(@RequestParam("userId") Integer userId,
			@RequestParam("identityType") UserOauthEnum.IdentityType identityType);

	@RequestMapping("/bindOauth")
	Result<Boolean> bindOauth(@RequestBody UserOauthReq userInfoReq);

}
