package com.company.admin.service.marketing;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.service.IService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.company.admin.entity.marketing.UserCoupon;
import com.company.admin.mapper.marketing.UserCouponDao;

@Service
public class UserCouponService extends ServiceImpl<UserCouponDao, UserCoupon>
		implements IService<UserCoupon> {

}