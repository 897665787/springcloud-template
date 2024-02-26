package com.company.tool.api.feign.fallback;

import org.springframework.stereotype.Component;

import com.company.common.api.Result;
import com.company.tool.api.feign.SubOrderDemo3Feign;
import com.company.tool.api.request.SubOrderDemo3OrderReq;

import feign.hystrix.FallbackFactory;

@Component
public class SubOrderDemo3FeignFallback implements FallbackFactory<SubOrderDemo3Feign> {

	@Override
	public SubOrderDemo3Feign create(final Throwable e) {
		return new SubOrderDemo3Feign() {
			@Override
			public Result<?> buy(SubOrderDemo3OrderReq subOrderDemo3OrderReq) {
				return Result.onFallbackError();
			}
		};
	}
}
