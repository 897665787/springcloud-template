package com.company.tool.service.market;

import java.time.LocalDateTime;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.company.tool.api.enums.PopupEnum;
import com.company.tool.entity.PopupLog;
import com.company.tool.mapper.market.PopupLogMapper;

@Component
public class PopupLogService extends ServiceImpl<PopupLogMapper, PopupLog> implements IService<PopupLog> {

	public LocalDateTime lastPopupTime(Integer userId, String deviceid) {
		LocalDateTime lastPopupLogByUserId = null;
		if (userId != null && userId > 0) {
			lastPopupLogByUserId = baseMapper.lastByUserId(userId);
		}
		LocalDateTime lastPopupLogByDeviceid = null;
		if (StringUtils.isNotBlank((deviceid))) {
			lastPopupLogByDeviceid = baseMapper.lastByDeviceid(deviceid);
		}
		if (lastPopupLogByUserId == null) {
			return lastPopupLogByDeviceid;
		}
		if (lastPopupLogByDeviceid == null) {
			return lastPopupLogByUserId;
		}

		if (lastPopupLogByUserId.isAfter(lastPopupLogByDeviceid)) {
			return lastPopupLogByUserId;
		} else {
			return lastPopupLogByDeviceid;
		}
	}

	public LocalDateTime lastPopupTimeByBusiness(PopupEnum.LogBusinessType businessType, Integer businessId, Integer userId,
			String deviceid) {
		if (userId == null || userId < 1) {
			userId = -1;// 赋一个不存在的值
		}
		if (StringUtils.isBlank(deviceid)) {
			deviceid = "NO";// 赋一个不存在的值
		}
		return baseMapper.lastByBusiness(businessType, businessId, userId, deviceid);
	}

	public Integer countByBusiness(PopupEnum.LogBusinessType businessType, Integer businessId, Integer userId, String deviceid,
			LocalDateTime beginTime, LocalDateTime endTime) {
		if (userId == null || userId < 1) {
			userId = -1;// 赋一个不存在的值
		}
		if (StringUtils.isBlank(deviceid)) {
			deviceid = "NO";// 赋一个不存在的值
		}
		return baseMapper.countByBusiness(businessType, businessId, userId, deviceid, beginTime, endTime);
	}
}
