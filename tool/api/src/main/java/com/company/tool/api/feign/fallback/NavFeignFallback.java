package com.company.tool.api.feign.fallback;

import java.util.List;

import com.company.common.api.Result;
import org.springframework.stereotype.Component;


import com.company.tool.api.feign.NavFeign;
import com.company.tool.api.request.NavReq;
import com.company.tool.api.response.NavResp;

import org.springframework.cloud.openfeign.FallbackFactory;

@Component
public class NavFeignFallback implements FallbackFactory<NavFeign> {

	@Override
	public NavFeign create(final Throwable e) {
		return new NavFeign() {
			@Override
			public List<NavResp> list(NavReq navReq) {
				return Result.onFallbackError();
			}
		};
	}
}
