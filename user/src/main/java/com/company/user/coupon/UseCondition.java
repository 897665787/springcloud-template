package com.company.user.coupon;

import java.util.List;

import com.company.user.coupon.dto.MatchResult;

public interface UseCondition {
	/**
	 * 判断用户能否看见该优惠券（例：需要根据客户端判断券是否展示）
	 * 
	 * @param seeParam
	 * @return 返回 券是否可见，默认可见
	 */
	default boolean canSee(SeeParam seeParam) {
		return true;
	}
	
	/**
	 * 判断优惠券是否可用
	 * 
	 * @param useParam
	 * @return 优惠券是否可用
	 */
	MatchResult canUse(UseParam useParam);

	/**
	 * 过滤商品
	 * 
	 * <pre>
	 * 注意：该方法的调用方应该是定时任务或MQ消费者，对性能要求不高，但要稳定，需要防止查询条件过于简单导致慢sql，建议在实现类中使用分页查询
	 * </pre>
	 * 
	 * @param filterParam
	 * @return 返回 券可用商品编码列表，返回 null表示不过滤商品
	 */
	default List<String> filterProduct(FilterParam filterParam) {
		return null;
	}
}