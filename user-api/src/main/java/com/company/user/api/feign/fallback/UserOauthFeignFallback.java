package com.company.user.api.feign.fallback;

import org.springframework.stereotype.Component;

import com.company.common.api.Result;
import com.company.user.api.enums.UserOauthEnum.IdentityType;
import com.company.user.api.feign.UserOauthFeign;
import com.company.user.api.request.UserOauthReq;
import com.company.user.api.response.UserOauthResp;

import org.springframework.cloud.openfeign.FallbackFactory;

@Component
public class UserOauthFeignFallback implements FallbackFactory<UserOauthFeign> {

	@Override
	public UserOauthFeign create(final Throwable e) {
		return new UserOauthFeign() {
			@Override
			public Result<Boolean> bindOauth(UserOauthReq userInfoReq) {
				return Result.onFallbackError();
			}

			@Override
			public Result<UserOauthResp> selectOauth(IdentityType identityType, String identifier) {
				return Result.onFallbackError();
			}

			@Override
			public Result<String> selectCertificate(Integer userId, IdentityType identityType) {
				return Result.onFallbackError();
			}
			
			@Override
			public Result<String> selectIdentifier(Integer userId, IdentityType identityType) {
				return Result.onFallbackError();
			}

		};
	}
}
