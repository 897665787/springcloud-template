package com.company.user.service.market;

import org.springframework.stereotype.Component;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.company.user.entity.CouponGrant;
import com.company.user.mapper.market.CouponGrantMapper;

@Component
public class CouponGrantService extends ServiceImpl<CouponGrantMapper, CouponGrant> implements IService<CouponGrant> {

}
