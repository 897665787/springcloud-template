package com.company.user.coupon;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.company.framework.context.SpringContextUtil;
import com.company.user.coupon.dto.MatchResult;
import com.company.user.coupon.dto.UserCouponCanUse;
import com.company.user.coupon.dto.UserCouponMe;
import com.company.user.coupon.util.CollectionUtil;
import com.company.user.entity.CouponTemplateCondition;
import com.company.user.entity.UserCoupon;
import com.company.user.service.market.CouponTemplateConditionService;
import com.company.user.service.market.UserCouponService;
import com.google.common.collect.Lists;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class UseCouponService {

	@Autowired
	private UserCouponService userCouponService;
	@Autowired
	private CouponTemplateConditionService couponTemplateConditionService;

	@Value("${usecoupon.parallel.execute:true}")
	private boolean parallelExecute;// 并发执行情况下日志ID不好追踪，特做此开关，默认开启
	
	/**
	 * <pre>
	 * 用户优惠券列表（未使用、即将过期、已使用、已过期）
	 * 应用场景：用户查看自己的优惠券列表
	 * </pre>
	 * 
	 * @param userId
	 * @param runtimeAttach
	 * @return
	 */
	public List<UserCouponMe> listCouponByAppUserId(Integer userId, String status, Map<String, String> runtimeAttach) {
		List<UserCoupon> userCouponList = userCouponService.selectByUserIdStatus(userId, status);
		
		List<UserCouponMe> addCanUseList = userCouponList.stream().map(userCoupon -> {
			Integer userCouponId = userCoupon.getId();

			List<CouponTemplateCondition> couponTemplateConditionList = couponTemplateConditionService
					.selectByCouponTemplateId(userCoupon.getCouponTemplateId());

			if (couponTemplateConditionList.isEmpty()) {
				log.warn("优惠券模板{}使用条件未配置", userCoupon.getCouponTemplateId());
				return null;
			}
			
			if (!parallelExecute) {
				/*
				 * 串行执行条件判断，任意1个匹配false则返回false，否则返回true
				 */
				for (CouponTemplateCondition v : couponTemplateConditionList) {
					String beanName = v.getUseCondition();
					UseCondition condition = SpringContextUtil.getBean(beanName, UseCondition.class);
					if (condition == null) {
						log.warn("使用条件未配置:{}", beanName);
						return null;
					}
					SeeParam seeParam = SeeParam.builder().userCouponId(userCouponId).userId(userId)
							.runtimeAttach(runtimeAttach).useConditionValue(v.getUseConditionValue()).build();
					boolean canSee = condition.canSee(seeParam);
					if (!canSee) {
						return null;
					}
				}
				
				/* 如果有性能问题，可以替换为以下并发执行代码，不过同时也会有日志ID不好追踪的问题 */
			} else {
				/*
				 * 并发执行条件判断，任意1个匹配false则返回false，否则返回true
				 */
				List<Supplier<Boolean>> supplierList = couponTemplateConditionList.stream().map(v -> {
					Supplier<Boolean> supplier = () -> {
						String beanName = v.getUseCondition();
						UseCondition condition = SpringContextUtil.getBean(beanName, UseCondition.class);
						if (condition == null) {
							log.warn("使用条件未配置:{}", beanName);
							return false;
						}
						SeeParam seeParam = SeeParam.builder().userCouponId(userCouponId).userId(userId)
								.runtimeAttach(runtimeAttach).useConditionValue(v.getUseConditionValue()).build();
						return condition.canSee(seeParam);
					};
					return supplier;
				}).collect(Collectors.toList());

				// 任意1个匹配false则返回false，否则返回true
				boolean result = anyMatch(supplierList, false);
				if (!result) {
					return null;
				}
			}

			UserCouponMe userCouponMe = new UserCouponMe();
			userCouponMe.setUserCouponId(userCoupon.getId());
			userCouponMe.setName(userCoupon.getName());
			userCouponMe.setMaxAmount(userCoupon.getMaxAmount());
			userCouponMe.setDiscount(userCoupon.getDiscount());
			userCouponMe.setConditionAmount(userCoupon.getConditionAmount());
			userCouponMe.setEndTime(userCoupon.getEndTime());
			return userCouponMe;
		}).filter(v -> v != null).collect(Collectors.toList());

		return addCanUseList;
	}
	
	/**
	 * <pre>
	 * 用户最优的可用优惠券
	 * 应用场景：商品展示券后价、支付时自动为用户选择优惠券
	 * </pre>
	 * 
	 * @param userId
	 * @param orderAmount
	 * @param runtimeAttach
	 * @return
	 */
	public UserCouponCanUse bestCouponCanUse(Integer userId, BigDecimal orderAmount, Map<String, String> runtimeAttach) {
		List<UserCouponCanUse> userCouponList = this.listCouponCanUseByAppUserId(userId, orderAmount, runtimeAttach);

		UserCouponCanUse userCouponDto = userCouponList.stream().filter(v -> v.getCanUse())
				.max((a, b) -> a.getReduceAmount().compareTo(b.getReduceAmount())).orElse(null);

		return userCouponDto;
	}

	/**
	 * <pre>
	 * 用户优惠券列表
	 * 应用场景：支付时展示选择优惠券的列表
	 * </pre>
	 * 
	 * @param userId
	 * @param orderAmount
	 * @param runtimeAttach
	 * @return
	 */
	public List<UserCouponCanUse> listCouponCanUseByAppUserId(Integer userId, BigDecimal orderAmount,
			Map<String, String> runtimeAttach) {
		List<UserCouponMe> userCouponMeList = listCouponByAppUserId(userId, "nouse", runtimeAttach);

		Collections.sort(userCouponMeList, (a, b) -> {
			// 先按金额倒序，金额一致按过期时间正序
			int compareTo = b.getMaxAmount().compareTo(a.getMaxAmount());
			if (compareTo == 0) {
				compareTo = a.getEndTime().compareTo(b.getEndTime());
			}
			return compareTo;
		});
		
		List<UserCouponCanUse> addCanUseList = userCouponMeList.stream().map(v -> {
			return this.canUse(v.getUserCouponId(), userId, orderAmount, runtimeAttach);
		}).collect(Collectors.toList());

		return addCanUseList;
	}

	/**
	 * <pre>
	 * 判断优惠券能否使用
	 * 应用场景：创建订单时校验优惠券
	 * </pre>
	 * 
	 * @param userCouponId
	 * @param userId
	 * @param orderAmount
	 * @param runtimeAttach
	 * @return
	 */
	public UserCouponCanUse canUse(Integer userCouponId, Integer userId, BigDecimal orderAmount, Map<String, String> runtimeAttach) {
		UserCoupon userCoupon = userCouponService.selectById(userCouponId);
		if (userCoupon == null) {
			UserCouponCanUse userCouponCanUse = new UserCouponCanUse();
			userCouponCanUse.setUserCouponId(userCouponId);
			userCouponCanUse.setReduceAmount(BigDecimal.ZERO);
			userCouponCanUse.setCanUse(false);
			userCouponCanUse.setReason("优惠券不存在");
			return userCouponCanUse;
		}
		
		List<CouponTemplateCondition> couponTemplateConditionList = couponTemplateConditionService
				.selectByCouponTemplateId(userCoupon.getCouponTemplateId());
		
		UserCouponCanUse userCouponCanUse = new UserCouponCanUse();
		userCouponCanUse.setUserCouponId(userCouponId);
		
		BigDecimal reduceAmount = userCoupon.getMaxAmount();
		BigDecimal discount = userCoupon.getDiscount();
		if (discount.compareTo(BigDecimal.ONE) < 0) {// 折扣
			BigDecimal discountAmount = orderAmount.multiply(BigDecimal.ONE.subtract(discount));
			reduceAmount = discountAmount;
		} else {// 直接扣减
			reduceAmount = userCoupon.getMaxAmount();
		}
		userCouponCanUse.setReduceAmount(reduceAmount);
		
		if (couponTemplateConditionList.isEmpty()) {
			log.warn("优惠券模板{}使用条件未配置", userCoupon.getCouponTemplateId());
			userCouponCanUse.setCanUse(false);
			return userCouponCanUse;
		}

		if (!parallelExecute) {
			/*
			 * 串行执行条件判断，任意1个匹配false则返回false，否则返回true
			 */
			for (CouponTemplateCondition v : couponTemplateConditionList) {
				String beanName = v.getUseCondition();
				UseCondition condition = SpringContextUtil.getBean(beanName, UseCondition.class);
				if (condition == null) {
					log.warn("使用条件未配置:{}", beanName);
					userCouponCanUse.setCanUse(false);
					return userCouponCanUse;
				}
				UseParam useParam = UseParam.builder().userCouponId(userCouponId).userId(userId)
						.orderAmount(orderAmount).runtimeAttach(runtimeAttach)
						.useConditionValue(v.getUseConditionValue()).build();

				MatchResult matchResult = condition.canUse(useParam);
				if (!matchResult.getCanUse()) {
					userCouponCanUse.setCanUse(false);
					userCouponCanUse.setReason(matchResult.getReason());
					return userCouponCanUse;
				}
			}

			userCouponCanUse.setCanUse(true);
			return userCouponCanUse;

			/* 如果有性能问题，可以替换为以下并发执行代码，不过同时也会有日志ID不好追踪的问题 */
		} else {
			/*
			 * 并发执行条件判断，任意1个匹配false则返回false，否则返回true
			 */
			List<Supplier<MatchResult>> supplierList = couponTemplateConditionList.stream().map(v -> {
				Supplier<MatchResult> supplier = () -> {
					String beanName = v.getUseCondition();
					UseCondition condition = SpringContextUtil.getBean(beanName, UseCondition.class);
					if (condition == null) {
						log.warn("使用条件未配置:{}", beanName);
						return MatchResult.builder().canUse(false).reason("使用条件未配置").build();
					}
					UseParam useParam = UseParam.builder().userCouponId(userCouponId).userId(userId)
							.orderAmount(orderAmount).runtimeAttach(runtimeAttach).useConditionValue(v.getUseConditionValue()).build();
					return condition.canUse(useParam);
				};
				return supplier;
			}).collect(Collectors.toList());

			// 任意1个匹配false则返回false，否则返回true
			MatchResult result = anyMatch(supplierList, false, MatchResult.builder().canUse(true).build());
			userCouponCanUse.setCanUse(result.getCanUse());
			userCouponCanUse.setReason(result.getReason());
			return userCouponCanUse;
		}
	}

	/**
	 * 任意1个匹配expect则返回,否则返回successResult
	 * 
	 * @param supplierList
	 * @param expect
	 * @param successResult
	 * @return
	 */
	private static MatchResult anyMatch(List<Supplier<MatchResult>> supplierList, boolean expect, MatchResult successResult) {
		// 构建CompletableFuture
		List<CompletableFuture<MatchResult>> completableFutureList = supplierList.stream()
				.map(v -> CompletableFuture.supplyAsync(v)).collect(Collectors.toList());

		CompletableFuture<MatchResult> result = new CompletableFuture<>();
		
		CompletableFuture.allOf(completableFutureList.stream().map(f -> f.thenAccept(v -> {
			if (expect == v.getCanUse())
				result.complete(v);
		})).toArray(CompletableFuture<?>[]::new)).whenComplete((ignored, t) -> result.complete(successResult));

		return result.join();
	}
	
	/**
	 * 任意1个匹配则返回
	 * 
	 * @param supplierList
	 * @param expect
	 * @return
	 */
	private static boolean anyMatch(List<Supplier<Boolean>> supplierList, boolean expect) {
		// 构建CompletableFuture
		List<CompletableFuture<Boolean>> completableFutureList = supplierList.stream()
				.map(v -> CompletableFuture.supplyAsync(v)).collect(Collectors.toList());

		// 构建预期值
		Predicate<Boolean> predicate = Predicate.isEqual(expect);

		CompletableFuture<Boolean> result = new CompletableFuture<>();

		CompletableFuture.allOf(completableFutureList.stream().map(f -> f.thenAccept(v -> {
			if (predicate.test(v))
				result.complete(v);
		})).toArray(CompletableFuture<?>[]::new)).whenComplete((ignored, t) -> result.complete(!expect));

		return result.join();
	}

	public static void main(String[] args) {
		List<Supplier<MatchResult>> supplierList = Lists.newArrayList();
		supplierList.add(() -> {
			try {
				Thread.sleep(1000);
			} catch (Exception e) {
				e.printStackTrace();
			}
			System.out.println("1000 done.");
//			return MatchResult.builder().canUse(false).reason("1111111111").build();
			return MatchResult.builder().canUse(true).build();
		});
		supplierList.add(() -> {
			try {
				Thread.sleep(2000);
			} catch (Exception e) {
				e.printStackTrace();
			}
			System.out.println("2000 done.");
//			return MatchResult.builder().canUse(false).reason("22222222222").build();
			return MatchResult.builder().canUse(true).build();
		});
		supplierList.add(() -> {
			try {
				Thread.sleep(3000);
			} catch (Exception e) {
				e.printStackTrace();
			}
			System.out.println("3000 done.");
			return MatchResult.builder().canUse(true).build();
		});

		long start = System.currentTimeMillis();
		MatchResult result = anyMatch(supplierList, false, MatchResult.builder().canUse(true).build());
		System.out.println("result:" + result + "    " + (System.currentTimeMillis() - start));
	}
	
	public static void main2(String[] args) {
		List<Supplier<Boolean>> supplierList = Lists.newArrayList();
		supplierList.add(() -> {
			try {
				Thread.sleep(1000);
			} catch (Exception e) {
				e.printStackTrace();
			}
			System.out.println("1000 done.");
			return true;
		});
		supplierList.add(() -> {
			try {
				Thread.sleep(2000);
			} catch (Exception e) {
				e.printStackTrace();
			}
			System.out.println("2000 done.");
			return false;
		});
		supplierList.add(() -> {
			try {
				Thread.sleep(3000);
			} catch (Exception e) {
				e.printStackTrace();
			}
			System.out.println("3000 done.");
			return true;
		});

		long start = System.currentTimeMillis();
		boolean result = anyMatch(supplierList, false);
		System.out.println("result:" + result + "    " + (System.currentTimeMillis() - start));
	}
	
	/**
	 * <pre>
	 * 商品过滤
	 * 应用场景：券适用商品列表
	 * </pre>
	 * 
	 * @param couponTemplateId
	 * @return 商品编码列表
	 */
	public List<String> filterProduct(Integer couponTemplateId) {
		// 查找有效的优惠券模板
		List<CouponTemplateCondition> couponTemplateConditionList = couponTemplateConditionService
				.selectByCouponTemplateId(couponTemplateId);

		if (couponTemplateConditionList.isEmpty()) {
			log.warn("优惠券模板{}使用条件未配置", couponTemplateId);
			return new ArrayList<String>();
		}
		
		List<List<String>> productCodeListList = couponTemplateConditionList.parallelStream().map(v -> {
			String beanName = v.getUseCondition();
			UseCondition condition = SpringContextUtil.getBean(beanName, UseCondition.class);
			if (condition == null) {
				log.warn("使用条件未配置:{}", beanName);
				return new ArrayList<String>();
			}
			FilterParam filterParam = FilterParam.builder().couponTemplateId(couponTemplateId)
					.useConditionValue(v.getUseConditionValue()).build();
			List<String> productCodeList = condition.filterProduct(filterParam);
			return productCodeList;
		}).filter(v -> v != null).collect(Collectors.toList());

		if (productCodeListList.isEmpty()) {
			return null; // 返回null代表所有商品可用
		}

		return CollectionUtil.intersection(productCodeListList);
	}
}
