package com.company.tool.api.feign.fallback;

import java.util.List;

import org.springframework.stereotype.Component;

import com.company.common.api.Result;
import com.company.tool.api.feign.NavFeign;
import com.company.tool.api.request.NavReq;
import com.company.tool.api.response.NavResp;

import feign.hystrix.FallbackFactory;

@Component
public class NavFeignFallback implements FallbackFactory<NavFeign> {

	@Override
	public NavFeign create(final Throwable e) {
		return new NavFeign() {
			@Override
			public Result<List<NavResp>> list(NavReq navReq) {
				return Result.onFallbackError();
			}
		};
	}
}
