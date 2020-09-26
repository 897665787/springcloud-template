package com.company.user.api.feign.fallback;

import org.springframework.stereotype.Component;

import com.company.user.api.feign.UserFeign;
import com.company.user.api.response.UserResp;

import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class UserFeignFallback implements FallbackFactory<UserFeign> {

	@Override
	public UserFeign create(final Throwable e) {
		return new UserFeign() {
			public UserResp getById(Long id) {
				log.error("getById error", e);
				return new UserResp().setOrderCode("Fallback");
			}
		};
	}
}
