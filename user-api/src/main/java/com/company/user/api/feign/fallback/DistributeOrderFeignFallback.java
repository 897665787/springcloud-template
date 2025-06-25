package com.company.user.api.feign.fallback;

import org.springframework.stereotype.Component;

import com.company.common.api.Result;
import com.company.user.api.feign.DistributeOrderFeign;
import com.company.user.api.request.DistributeBuyOrderReq;
import com.company.user.api.response.DistributeBuyOrderResp;

import org.springframework.cloud.openfeign.FallbackFactory;

@Component
public class DistributeOrderFeignFallback implements FallbackFactory<DistributeOrderFeign> {

	@Override
	public DistributeOrderFeign create(final Throwable e) {
		return new DistributeOrderFeign() {
			@Override
			public DistributeBuyOrderResp buy(DistributeBuyOrderReq distributeBuyOrderReq) {
				return Result.onFallbackError();
			}
		};
	}
}
