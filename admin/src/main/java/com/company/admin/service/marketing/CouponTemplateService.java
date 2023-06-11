package com.company.admin.service.marketing;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.service.IService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.company.admin.entity.marketing.CouponTemplate;
import com.company.admin.mapper.marketing.CouponTemplateDao;

@Service
public class CouponTemplateService extends ServiceImpl<CouponTemplateDao, CouponTemplate>
		implements IService<CouponTemplate> {

}