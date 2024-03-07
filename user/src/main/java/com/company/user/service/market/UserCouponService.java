package com.company.user.service.market;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.baomidou.mybatisplus.service.IService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.company.common.exception.BusinessException;
import com.company.framework.amqp.MessageSender;
import com.company.framework.amqp.rabbit.constants.FanoutConstants;
import com.company.user.entity.CouponTemplate;
import com.company.user.entity.UserCoupon;
import com.company.user.mapper.market.UserCouponMapper;
import com.google.common.collect.Maps;

import cn.hutool.core.date.LocalDateTimeUtil;

@Component
public class UserCouponService extends ServiceImpl<UserCouponMapper, UserCoupon> implements IService<UserCoupon> {

	@Autowired
	private CouponTemplateService couponTemplateService;
	@Autowired
	private MessageSender messageSender;

	public List<UserCoupon> selectByUserIdStatus(Integer userId, String status) {
		return baseMapper.selectByUserIdStatus(userId, status);
	}

	public void sendCoupon(Integer userId, Integer couponTemplateId) {
		CouponTemplate couponTemplate = couponTemplateService.selectById(couponTemplateId);
		if (couponTemplate.getEndTime().isBefore(LocalDateTime.now())) {
			throw new BusinessException("优惠券模板已过期");
		}

		UserCoupon userCoupon = new UserCoupon();
		userCoupon.setUserId(userId);
		userCoupon.setCouponTemplateId(couponTemplateId);
		userCoupon.setName(couponTemplate.getName());
		userCoupon.setMaxAmount(couponTemplate.getMaxAmount());
		userCoupon.setDiscount(couponTemplate.getDiscount());
		userCoupon.setConditionAmount(couponTemplate.getConditionAmount());
		userCoupon.setBeginTime(LocalDateTime.now());

//		String periodType = couponTemplate.getPeriodType();
		LocalDateTime endTime = LocalDateTimeUtil.endOfDay(userCoupon.getBeginTime().plusDays(3));
		userCoupon.setEndTime(endTime);
		userCoupon.setStatus("nouse");
		userCoupon.setMaxNum(1);

		baseMapper.insert(userCoupon);

		// 发布优惠券发放事件
		Map<String, Object> params = Maps.newHashMap();
		params.put("userCouponId", userCoupon.getId());
		params.put("userId", userId);
		messageSender.sendFanoutMessage(params, FanoutConstants.SEND_COUPON.EXCHANGE);
	}

	public Integer updateStatus(Integer id, String oldStatus, String newStatus) {
		return baseMapper.updateStatus(id, oldStatus, newStatus);
	}
}
