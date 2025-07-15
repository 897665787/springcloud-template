package com.company.system.messagedriven.strategy;

import java.time.LocalDateTime;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.company.framework.messagedriven.BaseStrategy;
import com.company.system.entity.SysUserPassword;
import com.company.system.service.SysUserPasswordService;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.LocalDateTimeUtil;

/**
 * 增加过期后登录次数
 */
@Component(StrategyConstants.INCR_EXPIRELOGINTIMES_STRATEGY)
public class IncrExpireLoginTimesStrategy implements BaseStrategy<Map<String, Object>> {

	@Autowired
	private SysUserPasswordService sysUserPasswordService;

	@Override
	public void doStrategy(Map<String, Object> params) {
		Integer sysUserId = MapUtils.getInteger(params, "sysUserId");

		SysUserPassword sysUserPassword = sysUserPasswordService.getLastBySysUserId(sysUserId);
		if (sysUserPassword == null) {
			// 密码未配置
			return;
		}

		LocalDateTime loginTime = LocalDateTimeUtil.parse(MapUtils.getString(params, "loginTime"),
				DatePattern.NORM_DATETIME_FORMATTER);
		if (sysUserPassword.getExpireTime().compareTo(loginTime) > 0) {// 未过期
			return;
		}

		// 增加登录次数
		sysUserPasswordService.incrExpireLoginTimes(sysUserPassword.getId());
	}
}
