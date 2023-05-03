package com.company.user.service.market;

import java.util.List;

import org.springframework.stereotype.Component;

import com.baomidou.mybatisplus.service.IService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.company.user.entity.CouponTemplateCondition;
import com.company.user.mapper.market.CouponTemplateConditionMapper;

@Component
public class CouponTemplateConditionService extends ServiceImpl<CouponTemplateConditionMapper, CouponTemplateCondition>
		implements IService<CouponTemplateCondition> {

	public List<CouponTemplateCondition> selectByCouponTemplateId(Integer couponTemplateId) {
		return baseMapper.selectByCouponTemplateId(couponTemplateId);
	}

}
