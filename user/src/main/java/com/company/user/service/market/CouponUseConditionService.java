package com.company.user.service.market;

import org.springframework.stereotype.Component;

import com.baomidou.mybatisplus.service.IService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.company.user.entity.CouponUseCondition;
import com.company.user.mapper.market.CouponUseConditionMapper;

@Component
public class CouponUseConditionService extends ServiceImpl<CouponUseConditionMapper, CouponUseCondition> implements IService<CouponUseCondition> {

}
