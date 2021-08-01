package com.company.web.service;

import org.springframework.stereotype.Service;

import cn.hutool.http.HttpUtil;

@Service
public class SuningTimeService implements TimeService {

	@Override
	public String getTime() {
		return HttpUtil.get("http://quan.suning.com/getSysTime.do");
	}
}
