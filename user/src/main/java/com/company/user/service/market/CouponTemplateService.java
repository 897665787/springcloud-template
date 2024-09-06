package com.company.user.service.market;

import org.springframework.stereotype.Component;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.company.user.entity.CouponTemplate;
import com.company.user.mapper.market.CouponTemplateMapper;

@Component
public class CouponTemplateService extends ServiceImpl<CouponTemplateMapper, CouponTemplate> implements IService<CouponTemplate> {

}
