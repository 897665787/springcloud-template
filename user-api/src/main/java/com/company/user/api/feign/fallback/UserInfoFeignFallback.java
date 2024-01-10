package com.company.user.api.feign.fallback;

import org.springframework.stereotype.Component;

import com.company.common.api.Result;
import com.company.user.api.feign.UserInfoFeign;
import com.company.user.api.request.UserInfoReq;
import com.company.user.api.response.UserInfoResp;

import feign.hystrix.FallbackFactory;

@Component
public class UserInfoFeignFallback implements FallbackFactory<UserInfoFeign> {

	@Override
	public UserInfoFeign create(final Throwable e) {
		return new UserInfoFeign() {
			@Override
			public Result<UserInfoResp> findOrCreateUser(UserInfoReq userInfoReq) {
				return Result.onFallbackError();
			}
		};
	}
}
