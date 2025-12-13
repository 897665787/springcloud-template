package com.company.user.service.market;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import com.company.framework.globalresponse.ExceptionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.company.framework.messagedriven.MessageSender;
import com.company.framework.messagedriven.constants.BroadcastConstants;
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
		CouponTemplate couponTemplate = couponTemplateService.getById(couponTemplateId);
		if (couponTemplate.getEndTime().isBefore(LocalDateTime.now())) {
			ExceptionUtil.throwException("优惠券模板已过期");
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
		messageSender.sendBroadcastMessage(params, BroadcastConstants.SEND_COUPON.EXCHANGE);
	}

	public Integer updateStatus(Integer id, String oldStatus, String newStatus) {
		if (id == null || id <= 0) {// 无效的用户优惠券id
			return 0;
		}
		return baseMapper.updateStatus(id, oldStatus, newStatus);
	}
}
