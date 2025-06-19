package com.company.user.api.feign.fallback;

import org.springframework.stereotype.Component;

import com.company.common.api.Result;
import com.company.user.api.feign.RechargeOrderFeign;
import com.company.user.api.request.RechargeOrderReq;
import com.company.user.api.response.RechargeOrderResp;

import org.springframework.cloud.openfeign.FallbackFactory;

@Component
public class RechargeOrderFeignFallback implements FallbackFactory<RechargeOrderFeign> {

	@Override
	public RechargeOrderFeign create(final Throwable e) {
		return new RechargeOrderFeign() {
			@Override
			public RechargeOrderResp buy(RechargeOrderReq rechargeOrderReq) {
				return Result.onFallbackError();
			}
		};
	}
}
