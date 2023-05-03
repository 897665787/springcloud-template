package com.company.user.controller;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.company.common.api.Result;
import com.company.user.coupon.UseCouponService;
import com.company.user.coupon.dto.UserCouponCanUse;
import com.company.user.coupon.dto.UserCouponMe;
import com.google.common.collect.Maps;

@RestController
@RequestMapping("/coupontest")
public class CouponTestController {

	@Autowired
	private UseCouponService useCouponService;

	/**
	 * <pre>
	 * 用户优惠券列表（未使用、即将过期、已使用、已过期）
	 * 应用场景：用户查看自己的优惠券列表
	 * </pre>
	 */
	@RequestMapping("/listCouponByAppUserId")
	public Result<?> listCouponByAppUserId(Integer userId) {
		String status = "nouse";

		Map<String, String> runtimeAttach = Maps.newHashMap();
		runtimeAttach.put("platform", "app");

		List<UserCouponMe> userCouponMeList = useCouponService.listCouponByAppUserId(userId, status, runtimeAttach);

		return Result.success(userCouponMeList);
	}

	/**
	 * <pre>
	 * 用户最优的可用优惠券
	 * 应用场景：商品展示券后价、支付时自动为用户选择优惠券
	 * </pre>
	 */
	@RequestMapping("/bestCouponCanUse")
	public Result<?> bestCouponCanUse(Integer userId) {
		Map<String, String> runtimeAttach = Maps.newHashMap();
		runtimeAttach.put("productCode", "AB3301");

		BigDecimal orderAmount = new BigDecimal("30");

		UserCouponCanUse bestCouponCanUse = useCouponService.bestCouponCanUse(userId, orderAmount, runtimeAttach);

		return Result.success(bestCouponCanUse);
	}

	/**
	 * <pre>
	 * 用户优惠券列表
	 * 应用场景：支付时展示选择优惠券的列表
	 * </pre>
	 */
	@RequestMapping("/listCouponCanUseByAppUserId")
	public Result<?> listCouponCanUseByAppUserId(Integer userId) {
		Map<String, String> runtimeAttach = Maps.newHashMap();
		runtimeAttach.put("productCode", "AB3301");
//		runtimeAttach.put("platform", "app");

		BigDecimal orderAmount = new BigDecimal("20");
		List<UserCouponCanUse> userCouponCanUseList = useCouponService.listCouponCanUseByAppUserId(userId, orderAmount,
				runtimeAttach);

		return Result.success(userCouponCanUseList);
	}

	/**
	 * <pre>
	 * 判断优惠券能否使用
	 * 应用场景：创建订单时校验优惠券
	 * </pre>
	 */
	@RequestMapping("/canUse")
	public Result<?> canUse(Integer userId, Integer userCouponId) {
		Map<String, String> runtimeAttach = Maps.newHashMap();
		runtimeAttach.put("productCode", "AB3301");

		BigDecimal orderAmount = new BigDecimal("20");
		UserCouponCanUse canUse = useCouponService.canUse(userCouponId, userId, orderAmount, runtimeAttach);

		return Result.success(canUse);
	}
}
