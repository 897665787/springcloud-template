package com.company.user.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.company.common.api.Result;
import com.company.common.util.PropertyUtils;
import com.company.user.api.feign.CouponFeign;
import com.company.user.api.response.UserCouponResp;
import com.company.user.entity.UserCoupon;
import com.company.user.service.market.UserCouponService;

@RestController
@RequestMapping("/coupon")
public class CouponController implements CouponFeign {

	@Autowired
	private UserCouponService userCouponService;

	@Override
	public Result<UserCouponResp> getUserCouponById(Integer userCouponId) {
		UserCoupon userCoupon = userCouponService.selectById(userCouponId);
		return Result.success(PropertyUtils.copyProperties(userCoupon, UserCouponResp.class));
	}

	@Override
	public Result<Boolean> isMatchTemplate(Integer userCouponId, Integer couponTemplateId) {
		UserCoupon userCoupon = userCouponService.selectById(userCouponId);
		if (userCoupon == null) {
			return Result.success(false);
		}
		return Result.success(userCoupon.getCouponTemplateId().equals(couponTemplateId));
	}
	
//	@Override
	public Result<UserCouponResp> select4Expire(Integer days) {
		// days天后即将过期的优惠券
//		LocalDateTime time = null;
//		userCouponService.selectWillExpire(time);
		UserCoupon userCoupon = userCouponService.selectById(1);
		return Result.success(PropertyUtils.copyProperties(userCoupon, UserCouponResp.class));
	}
}
