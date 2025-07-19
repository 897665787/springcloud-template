package com.company.user.api.feign.fallback;

import org.springframework.stereotype.Component;

import com.company.common.api.Result;
import com.company.user.api.feign.MemberBuyFeign;
import com.company.user.api.request.MemberBuyOrderReq;
import com.company.user.api.response.MemberBuyOrderResp;

import org.springframework.cloud.openfeign.FallbackFactory;

@Component
public class MemberBuyFeignFallback implements FallbackFactory<MemberBuyFeign> {

	@Override
	public MemberBuyFeign create(final Throwable e) {
		return new MemberBuyFeign() {
			@Override
			public Result<MemberBuyOrderResp> buy(MemberBuyOrderReq memberBuyOrderReq) {
				return Result.onFallbackError();
			}
		};
	}
}
