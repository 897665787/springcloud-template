package com.company.user.mapper.market;

import java.util.Collection;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.company.user.entity.CouponTemplateCondition;

public interface CouponTemplateConditionMapper extends BaseMapper<CouponTemplateCondition> {

	@Select("select * from mk_coupon_template_condition where coupon_template_id = #{couponTemplateId}")
	List<CouponTemplateCondition> selectByCouponTemplateId(@Param("couponTemplateId") Integer couponTemplateId);

	List<CouponTemplateCondition> selectByCouponTemplateIds(@Param("couponTemplateIds") Collection<Integer> couponTemplateIds);
}
