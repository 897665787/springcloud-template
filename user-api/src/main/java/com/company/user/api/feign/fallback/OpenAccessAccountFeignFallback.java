package com.company.user.api.feign.fallback;

import org.springframework.stereotype.Component;

import com.company.common.api.Result;
import com.company.user.api.feign.OpenAccessAccountFeign;

import org.springframework.cloud.openfeign.FallbackFactory;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class OpenAccessAccountFeignFallback implements FallbackFactory<OpenAccessAccountFeign> {

	@Override
	public OpenAccessAccountFeign create(final Throwable e) {
		return new OpenAccessAccountFeign() {
			@Override
			public String getAppKeyByAppid(String appid) {
				log.error("getAppKeyByAppid error", e);
				return Result.onFallbackError();
			}
		};
	}
}
