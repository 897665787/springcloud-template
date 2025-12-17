package com.company.web.service;

import com.company.framework.cache.ICache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.hutool.http.HttpUtil;

@Service
public class SuningTimeService implements TimeService {

	@Autowired
	private ICache cache;

	@Override
	public String getTime() {
		return HttpUtil.get("http://quan.suning.com/getSysTime.do");
	}

	@Override
	public String getCacheTime() {
		return cache.get("time", () -> {
			return HttpUtil.get("http://quan.suning.com/getSysTime.do");
		});
	}
}
