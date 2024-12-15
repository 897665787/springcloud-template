package com.company.user.service.market;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.company.user.entity.CouponTemplateCondition;
import com.company.user.mapper.market.CouponTemplateConditionMapper;

@Component
public class CouponTemplateConditionService extends ServiceImpl<CouponTemplateConditionMapper, CouponTemplateCondition>
		implements IService<CouponTemplateCondition> {

	public List<CouponTemplateCondition> selectByCouponTemplateId(Integer couponTemplateId) {
		return baseMapper.selectByCouponTemplateId(couponTemplateId);
	}

	public List<CouponTemplateCondition> selectByCouponTemplateIds(Collection<Integer> couponTemplateIds) {
		if (CollectionUtils.isEmpty(couponTemplateIds)) {
			return Collections.emptyList();
		}
		return baseMapper.selectByCouponTemplateIds(couponTemplateIds);
	}

}
