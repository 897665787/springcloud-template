package com.company.user.api.feign.fallback;

import org.springframework.stereotype.Component;

import com.company.common.api.Result;
import com.company.user.api.feign.MemberBuyFeign;
import com.company.user.api.request.MemberBuyOrderReq;

import feign.hystrix.FallbackFactory;

@Component
public class MemberBuyFeignFallback implements FallbackFactory<MemberBuyFeign> {

	@Override
	public MemberBuyFeign create(final Throwable e) {
		return new MemberBuyFeign() {
			@Override
			public Result<?> buy(MemberBuyOrderReq memberBuyOrderReq) {
				return Result.onFallbackError();
			}
		};
	}
}
