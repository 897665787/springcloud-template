package com.company.user.service.market;

import java.util.List;

import org.springframework.stereotype.Component;

import com.baomidou.mybatisplus.service.IService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.company.user.entity.UserCoupon;
import com.company.user.mapper.market.UserCouponMapper;

@Component
public class UserCouponService extends ServiceImpl<UserCouponMapper, UserCoupon> implements IService<UserCoupon> {

	public List<UserCoupon> selectByUserIdStatus(Integer userId, String status) {
		return baseMapper.selectByUserIdStatus(userId, status);
	}
}
