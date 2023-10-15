package com.company.user.api.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.company.common.api.Result;
import com.company.user.api.feign.fallback.CouponFeignFallback;
import com.company.user.api.response.UserCouponResp;

@FeignClient(value = "template-user", path = "/coupon", fallbackFactory = CouponFeignFallback.class)
public interface CouponFeign {

	@RequestMapping("/getUserCouponById")
	Result<UserCouponResp> getUserCouponById(@RequestParam("userCouponId") Integer userCouponId);

	@RequestMapping("/isMatchTemplate")
	Result<Boolean> isMatchTemplate(@RequestParam("userCouponId") Integer userCouponId,
			@RequestParam("couponTemplateId") Integer couponTemplateId);

}
