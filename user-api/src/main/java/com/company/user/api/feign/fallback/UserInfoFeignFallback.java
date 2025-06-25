package com.company.user.api.feign.fallback;

import org.springframework.stereotype.Component;

import com.company.common.api.Result;
import com.company.user.api.feign.UserInfoFeign;
import com.company.user.api.request.UserInfoReq;
import com.company.user.api.response.UserInfoResp;

import org.springframework.cloud.openfeign.FallbackFactory;

import java.util.Collection;
import java.util.Map;

@Component
public class UserInfoFeignFallback implements FallbackFactory<UserInfoFeign> {

	@Override
	public UserInfoFeign create(final Throwable e) {
		return new UserInfoFeign() {
			@Override
			public UserInfoResp findOrCreateUser(UserInfoReq userInfoReq) {
				return Result.onFallbackError();
			}

			@Override
			public UserInfoResp getById(Integer id) {
				return Result.onFallbackError();
			}

			@Override
			public Map<Integer, String> mapUidById(Collection<Integer> idList) {
				return Result.onFallbackError();
			}
		};
	}
}
