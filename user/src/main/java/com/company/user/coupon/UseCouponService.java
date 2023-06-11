package com.company.user.coupon;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.company.common.util.MdcUtil;
import com.company.framework.context.SpringContextUtil;
import com.company.user.coupon.dto.MatchResult;
import com.company.user.coupon.dto.UserCouponCanUse;
import com.company.user.coupon.dto.UserCouponCanUseBatch;
import com.company.user.coupon.dto.UserCouponCanUsePay;
import com.company.user.coupon.dto.UserCouponCanUseParam;
import com.company.user.coupon.dto.UserCouponMe;
import com.company.user.coupon.util.CollectionUtil;
import com.company.user.entity.CouponTemplateCondition;
import com.company.user.entity.UserCoupon;
import com.company.user.service.market.CouponTemplateConditionService;
import com.company.user.service.market.UserCouponService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class UseCouponService {

	@Autowired
	private UserCouponService userCouponService;
	@Autowired
	private CouponTemplateConditionService couponTemplateConditionService;

	/**
	 * <pre>
	 * 用户优惠券列表（未使用、即将过期、已使用、已过期）
	 * 应用场景：用户查看自己的优惠券列表
	 * </pre>
	 * 
	 * @param userId
	 * @param status
	 * @param seeRuntimeAttach
	 * @return
	 */
	public List<UserCouponMe> listCouponByAppUserId(Integer userId, String status,
			Map<String, String> seeRuntimeAttach) {
		log.info("userId:{},status:{},seeRuntimeAttach:{}", userId, status, JSON.toJSONString(seeRuntimeAttach));
		List<UserCoupon> userCouponList = userCouponService.selectByUserIdStatus(userId, status);

		Set<Integer> couponTemplateIdSet = userCouponList.stream().map(UserCoupon::getCouponTemplateId)
				.collect(Collectors.toSet());

		List<CouponTemplateCondition> couponTemplateConditionBatchList = couponTemplateConditionService
				.selectByCouponTemplateIds(couponTemplateIdSet);

		Map<Integer, List<CouponTemplateCondition>> couponTemplateIdConditionMap = couponTemplateConditionBatchList
				.stream().collect(Collectors.groupingBy(CouponTemplateCondition::getCouponTemplateId));

		List<UserCoupon> userCouponCanSeeList = this.canSee(userCouponList, couponTemplateIdConditionMap, userId,
				seeRuntimeAttach);

		List<UserCouponMe> userCouponMeList = userCouponCanSeeList.stream().map(userCoupon -> {
			UserCouponMe userCouponMe = new UserCouponMe();
			userCouponMe.setUserCouponId(userCoupon.getId());
			userCouponMe.setName(userCoupon.getName());
			userCouponMe.setMaxAmount(userCoupon.getMaxAmount());
			userCouponMe.setDiscount(userCoupon.getDiscount());
			userCouponMe.setConditionAmount(userCoupon.getConditionAmount());
			userCouponMe.setEndTime(userCoupon.getEndTime());
			return userCouponMe;
		}).filter(v -> v != null).collect(Collectors.toList());

		return userCouponMeList;
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
	public List<UserCouponCanUsePay> listCouponCanUseByAppUserId(Integer userId, BigDecimal orderAmount,
			Map<String, String> runtimeAttach) {
		log.info("userId:{},runtimeAttach:{}", userId, JSON.toJSONString(runtimeAttach));
		String status = "nouse";
		List<UserCoupon> userCouponList = userCouponService.selectByUserIdStatus(userId, status);

		Set<Integer> couponTemplateIdSet = userCouponList.stream().map(UserCoupon::getCouponTemplateId)
				.collect(Collectors.toSet());

		List<CouponTemplateCondition> couponTemplateConditionBatchList = couponTemplateConditionService
				.selectByCouponTemplateIds(couponTemplateIdSet);

		Map<Integer, List<CouponTemplateCondition>> couponTemplateIdConditionMap = couponTemplateConditionBatchList
				.stream().collect(Collectors.groupingBy(CouponTemplateCondition::getCouponTemplateId));

		List<UserCoupon> userCouponCanSeeList = this.canSee(userCouponList, couponTemplateIdConditionMap, userId,
				runtimeAttach);

		List<UserCouponCanUsePay> userCouponCanUsePayList = userCouponCanSeeList.stream().map(userCoupon -> {
			List<CouponTemplateCondition> couponTemplateConditionList = couponTemplateIdConditionMap
					.get(userCoupon.getCouponTemplateId());
			UserCouponCanUse userCouponCanUse = this.canUse(userCoupon, couponTemplateConditionList, userId,
					orderAmount, runtimeAttach);

			UserCouponCanUsePay userCouponCanUsePay = new UserCouponCanUsePay();
			userCouponCanUsePay.setUserCouponId(userCoupon.getId());
			userCouponCanUsePay.setName(userCoupon.getName());
			userCouponCanUsePay.setMaxAmount(userCoupon.getMaxAmount());
			userCouponCanUsePay.setDiscount(userCoupon.getDiscount());
			userCouponCanUsePay.setConditionAmount(userCoupon.getConditionAmount());
			userCouponCanUsePay.setEndTime(userCoupon.getEndTime());
			userCouponCanUsePay.setCanUse(userCouponCanUse.getCanUse());
			userCouponCanUsePay.setReduceAmount(userCouponCanUse.getReduceAmount());
			userCouponCanUsePay.setReason(userCouponCanUse.getReason());
			return userCouponCanUsePay;
		}).sorted((a, b) -> {
			// 先按金额倒序，金额一致按过期时间正序
			int compareTo = b.getReduceAmount().compareTo(a.getReduceAmount());
			if (compareTo == 0) {
				compareTo = a.getEndTime().compareTo(b.getEndTime());
			}
			return compareTo;
		}).collect(Collectors.toList());

		return userCouponCanUsePayList;
	}

	/**
	 * <pre>
	 * 用户最优的可用优惠券
	 * 应用场景：支付时自动为用户选择优惠券
	 * </pre>
	 * 
	 * @param userId
	 * @param orderAmount
	 * @param runtimeAttach
	 * @return
	 */
	public UserCouponCanUse bestCouponCanUse(Integer userId, BigDecimal orderAmount,
			Map<String, String> runtimeAttach) {
		log.info("userId:{},runtimeAttach:{}", userId, JSON.toJSONString(runtimeAttach));
		String status = "nouse";
		List<UserCoupon> userCouponList = userCouponService.selectByUserIdStatus(userId, status);

		Set<Integer> couponTemplateIdSet = userCouponList.stream().map(UserCoupon::getCouponTemplateId)
				.collect(Collectors.toSet());

		List<CouponTemplateCondition> couponTemplateConditionBatchList = couponTemplateConditionService
				.selectByCouponTemplateIds(couponTemplateIdSet);

		Map<Integer, List<CouponTemplateCondition>> couponTemplateIdConditionMap = couponTemplateConditionBatchList
				.stream().collect(Collectors.groupingBy(CouponTemplateCondition::getCouponTemplateId));

		List<UserCoupon> userCouponCanSeeList = this.canSee(userCouponList, couponTemplateIdConditionMap, userId,
				runtimeAttach);

		List<UserCouponCanUse> userCouponCanUseList = userCouponCanSeeList.stream().map(userCoupon -> {
			List<CouponTemplateCondition> couponTemplateConditionList = couponTemplateIdConditionMap
					.get(userCoupon.getCouponTemplateId());
			return this.canUse(userCoupon, couponTemplateConditionList, userId, orderAmount, runtimeAttach);
		}).collect(Collectors.toList());

		UserCouponCanUse bestCouponCanUse = userCouponCanUseList.stream().filter(v -> v.getCanUse()).max((a, b) -> {
			// 先按金额倒序，金额一致按过期时间正序
			int compareTo = b.getReduceAmount().compareTo(a.getReduceAmount());
			if (compareTo == 0) {
				compareTo = a.getEndTime().compareTo(b.getEndTime());
			}
			return compareTo;
		}).orElse(null);

		return bestCouponCanUse;
	}

	/**
	 * <pre>
	 * 用户最优的可用优惠券(批量)
	 * 应用场景：商品展示券后价
	 * </pre>
	 * 
	 * @param userId
	 * @param seeRuntimeAttach
	 * @param userCouponCanUseParamList
	 * @return
	 */
	public List<UserCouponCanUseBatch> bestCouponCanUseBatch(Integer userId, Map<String, String> seeRuntimeAttach,
			List<UserCouponCanUseParam> userCouponCanUseParamList) {
		log.info("userId:{},seeRuntimeAttach:{},userCouponCanUseParamList:{}", userId,
				JSON.toJSONString(seeRuntimeAttach), JSON.toJSONString(userCouponCanUseParamList));
		String status = "nouse";
		List<UserCoupon> userCouponList = userCouponService.selectByUserIdStatus(userId, status);

		Set<Integer> couponTemplateIdSet = userCouponList.stream().map(UserCoupon::getCouponTemplateId)
				.collect(Collectors.toSet());

		List<CouponTemplateCondition> couponTemplateConditionBatchList = couponTemplateConditionService
				.selectByCouponTemplateIds(couponTemplateIdSet);

		Map<Integer, List<CouponTemplateCondition>> couponTemplateIdConditionMap = couponTemplateConditionBatchList
				.stream().collect(Collectors.groupingBy(CouponTemplateCondition::getCouponTemplateId));

		List<UserCoupon> userCouponCanSeeList = this.canSee(userCouponList, couponTemplateIdConditionMap, userId,
				seeRuntimeAttach);

		List<UserCouponCanUseBatch> userCouponCanUseBatchList = userCouponCanUseParamList.stream()
				.map(userCouponCanUseParam -> {
					UserCouponCanUseBatch userCouponCanUseBatch = new UserCouponCanUseBatch();
					userCouponCanUseBatch.setUniqueCode(userCouponCanUseParam.getUniqueCode());

					List<UserCouponCanUse> userCouponCanUseList = userCouponCanSeeList.stream().map(userCoupon -> {
						List<CouponTemplateCondition> couponTemplateConditionList = couponTemplateIdConditionMap
								.get(userCoupon.getCouponTemplateId());
						return this.canUse(userCoupon, couponTemplateConditionList, userId,
								userCouponCanUseParam.getOrderAmount(), userCouponCanUseParam.getRuntimeAttach());
					}).collect(Collectors.toList());

					UserCouponCanUse bestCouponCanUse = userCouponCanUseList.stream().filter(v -> v.getCanUse())
							.max((a, b) -> {
								// 先按金额倒序，金额一致按过期时间正序
								int compareTo = b.getReduceAmount().compareTo(a.getReduceAmount());
								if (compareTo == 0) {
									compareTo = a.getEndTime().compareTo(b.getEndTime());
								}
								return compareTo;
							}).orElse(null);

					userCouponCanUseBatch.setUserCouponCanUse(bestCouponCanUse);

					return userCouponCanUseBatch;
				}).collect(Collectors.toList());

		return userCouponCanUseBatchList;
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
	public UserCouponCanUse canUse(Integer userCouponId, Integer userId, BigDecimal orderAmount,
			Map<String, String> runtimeAttach) {
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

		return canUse(userCoupon, couponTemplateConditionList, userId, orderAmount, runtimeAttach);
	}

	private List<UserCoupon> canSee(List<UserCoupon> userCouponList,
			Map<Integer, List<CouponTemplateCondition>> couponTemplateIdConditionMap, Integer userId,
			Map<String, String> seeRuntimeAttach) {

		List<UserCoupon> userCouponCanSeeList = userCouponList.stream().map(userCoupon -> {
			Integer userCouponId = userCoupon.getId();

			List<CouponTemplateCondition> couponTemplateConditionList = couponTemplateIdConditionMap
					.get(userCoupon.getCouponTemplateId());

			if (CollectionUtils.isEmpty(couponTemplateConditionList)) {
				log.warn("优惠券模板{}使用条件未配置", userCoupon.getCouponTemplateId());
				return null;
			}

			/*
			 * 并发执行条件判断，任意1个匹配false则返回false，否则返回true
			 */
			String traceId = MdcUtil.get();
			List<Supplier<Boolean>> supplierList = couponTemplateConditionList.stream().map(v -> {
				Supplier<Boolean> supplier = () -> {
					String subTraceId = MdcUtil.get();
					if (subTraceId == null) {
						MdcUtil.put(traceId);
					}
					String beanName = v.getUseCondition();
					UseCondition condition = SpringContextUtil.getBean(beanName, UseCondition.class);
					if (condition == null) {
						log.warn("使用条件未配置:{}", beanName);
						return false;
					}
					SeeParam seeParam = SeeParam.builder().userCouponId(userCouponId).userId(userId)
							.runtimeAttach(seeRuntimeAttach).useConditionValue(v.getUseConditionValue()).build();
					boolean canSee = condition.canSee(seeParam);
					if (subTraceId == null) {
						MdcUtil.remove();
					}
					return canSee;
				};
				return supplier;
			}).collect(Collectors.toList());

			// 任意1个匹配false则返回false，否则返回true
			boolean result = anyMatch(supplierList, false);
			if (!result) {
				return null;
			}
			return userCoupon;
		}).filter(v -> v != null).collect(Collectors.toList());
		log.info("canSee size:{}->{}", userCouponList.size(), userCouponCanSeeList.size());
		return userCouponCanSeeList;
	}

	private UserCouponCanUse canUse(UserCoupon userCoupon, List<CouponTemplateCondition> couponTemplateConditionList,
			Integer userId, BigDecimal orderAmount, Map<String, String> runtimeAttach) {
		UserCouponCanUse userCouponCanUse = new UserCouponCanUse();
		userCouponCanUse.setUserCouponId(userCoupon.getId());
		userCouponCanUse.setEndTime(userCoupon.getEndTime());

		BigDecimal reduceAmount = userCoupon.getMaxAmount();
		BigDecimal discount = userCoupon.getDiscount();
		if (discount.compareTo(BigDecimal.ONE) < 0) {// 折扣
			BigDecimal discountAmount = orderAmount.multiply(BigDecimal.ONE.subtract(discount));
			reduceAmount = discountAmount.min(reduceAmount);
		}
		userCouponCanUse.setReduceAmount(reduceAmount);

		if (CollectionUtils.isEmpty(couponTemplateConditionList)) {
			log.warn("优惠券模板{}使用条件未配置", userCoupon.getCouponTemplateId());
			userCouponCanUse.setCanUse(false);
			userCouponCanUse.setReason("优惠券模板" + userCoupon.getCouponTemplateId() + "使用条件未配置");
			return userCouponCanUse;
		}

		/*
		 * 并发执行条件判断，任意1个匹配false则返回false，否则返回true
		 */
		String traceId = MdcUtil.get();
		List<Supplier<MatchResult>> supplierList = couponTemplateConditionList.stream().map(v -> {
			Supplier<MatchResult> supplier = () -> {
				String subTraceId = MdcUtil.get();
				if (subTraceId == null) {
					MdcUtil.put(traceId);
				}
				String beanName = v.getUseCondition();
				UseCondition condition = SpringContextUtil.getBean(beanName, UseCondition.class);
				if (condition == null) {
					log.warn("使用条件未配置:{}", beanName);
					return MatchResult.builder().canUse(false).reason("使用条件未配置").build();
				}

				UseParam.UserCouponInfo userCouponInfo = UseParam.UserCouponInfo.builder()
						.userId(userCoupon.getUserId()).status(userCoupon.getStatus())
						.conditionAmount(userCoupon.getConditionAmount()).beginTime(userCoupon.getBeginTime())
						.endTime(userCoupon.getEndTime()).build();
				UseParam useParam = UseParam.builder().userCouponId(userCoupon.getId()).userCouponInfo(userCouponInfo)
						.userId(userId).orderAmount(orderAmount).runtimeAttach(runtimeAttach)
						.useConditionValue(v.getUseConditionValue()).build();

				MatchResult canUse = condition.canUse(useParam);
				if (subTraceId == null) {
					MdcUtil.remove();
				}
				return canUse;
			};
			return supplier;
		}).collect(Collectors.toList());

		// 任意1个匹配false则返回false，否则返回true
		MatchResult result = anyMatch(supplierList, false, MatchResult.builder().canUse(true).build());
		userCouponCanUse.setCanUse(result.getCanUse());
		userCouponCanUse.setReason(result.getReason());
		return userCouponCanUse;
	}

	/**
	 * 任意1个匹配expect则返回,否则返回successResult
	 * 
	 * @param supplierList
	 * @param expect
	 * @param successResult
	 * @return
	 */
	private static MatchResult anyMatch(List<Supplier<MatchResult>> supplierList, boolean expect,
			MatchResult successResult) {
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

		if (CollectionUtils.isEmpty(couponTemplateConditionList)) {
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
