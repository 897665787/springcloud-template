package com.company.user.api.feign.fallback;

import org.springframework.stereotype.Component;

import com.company.common.api.Result;
import com.company.common.api.ResultCode;
import com.company.user.api.feign.CouponFeign;
import com.company.user.api.response.UserCouponResp;

import feign.hystrix.FallbackFactory;

@Component
public class CouponFeignFallback implements FallbackFactory<CouponFeign> {

	@Override
	public CouponFeign create(final Throwable e) {
		return new CouponFeign() {
			@Override
			public Result<UserCouponResp> getUserCouponById(Integer userCouponId) {
				return Result.fail(ResultCode.FALLBACK);
			}

			@Override
			public Result<Boolean> isMatchTemplate(Integer userCouponId, Integer couponTemplateId) {
				return Result.fail(ResultCode.FALLBACK);
			}
		};
	}
}
