package com.company.admin.service.marketing;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.service.IService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.company.admin.entity.marketing.CouponTemplateCondition;
import com.company.admin.mapper.marketing.CouponTemplateConditionDao;

@Service
public class CouponTemplateConditionService extends ServiceImpl<CouponTemplateConditionDao, CouponTemplateCondition>
		implements IService<CouponTemplateCondition> {

}