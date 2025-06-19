package com.company.user.api.feign.fallback;

import org.springframework.stereotype.Component;

import com.company.common.api.Result;
import com.company.user.api.feign.UserFeign;
import com.company.user.api.request.UserReq;
import com.company.user.api.response.UserResp;

import org.springframework.cloud.openfeign.FallbackFactory;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class UserFeignFallback implements FallbackFactory<UserFeign> {

	@Override
	public UserFeign create(final Throwable e) {
		return new UserFeign() {
			public UserResp getById(Long id) {
				log.error("getById error", e);
				return Result.onFallbackError();
			}

			@Override
			public UserResp retryGet(Long id) {
				log.error("retryGet error", e);
				return Result.onFallbackError();
			}

			@Override
			public UserResp retryPost(UserReq userReq) {
				log.error("retryPost error", e);
				return Result.onFallbackError();
			}

			@Override
			public UserResp idempotent(UserReq userReq) {
				log.error("idempotent error", e);
				return Result.onFallbackError();
			}

			@Override
			public Void noreturn() {
				log.error("noreturn error", e);
				return Result.onFallbackError();
			}
		};
	}
}
