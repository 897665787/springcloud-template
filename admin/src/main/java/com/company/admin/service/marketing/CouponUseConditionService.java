package com.company.admin.service.marketing;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.service.IService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.company.admin.entity.marketing.CouponUseCondition;
import com.company.admin.mapper.marketing.CouponUseConditionDao;

@Service
public class CouponUseConditionService extends ServiceImpl<CouponUseConditionDao, CouponUseCondition>
		implements IService<CouponUseCondition> {

}