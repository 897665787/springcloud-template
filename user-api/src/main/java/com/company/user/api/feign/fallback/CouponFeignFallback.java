package com.company.user.api.feign.fallback;

import org.springframework.stereotype.Component;

import com.company.common.api.Result;
import com.company.user.api.feign.CouponFeign;
import com.company.user.api.response.UserCouponResp;

import org.springframework.cloud.openfeign.FallbackFactory;

@Component
public class CouponFeignFallback implements FallbackFactory<CouponFeign> {

	@Override
	public CouponFeign create(final Throwable e) {
		return new CouponFeign() {
			@Override
			public UserCouponResp getUserCouponById(Integer userCouponId) {
				return Result.onFallbackError();
			}

			@Override
			public Boolean isMatchTemplate(Integer userCouponId, Integer couponTemplateId) {
				return Result.onFallbackError();
			}
		};
	}
}
