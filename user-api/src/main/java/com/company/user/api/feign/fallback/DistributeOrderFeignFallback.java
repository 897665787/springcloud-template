package com.company.user.api.feign.fallback;

import org.springframework.stereotype.Component;

import com.company.common.api.Result;
import com.company.user.api.feign.DistributeOrderFeign;
import com.company.user.api.request.DistributeOrderReq;

import feign.hystrix.FallbackFactory;

@Component
public class DistributeOrderFeignFallback implements FallbackFactory<DistributeOrderFeign> {

	@Override
	public DistributeOrderFeign create(final Throwable e) {
		return new DistributeOrderFeign() {
			@Override
			public Result<?> buy(DistributeOrderReq distributeOrderReq) {
				return Result.onFallbackError();
			}
		};
	}
}
