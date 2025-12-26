package com.company.tool.api.feign.fallback;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.company.common.api.Result;
import org.springframework.stereotype.Component;


import com.company.tool.api.feign.SmsFeign;
import com.company.tool.api.request.SendSmsReq;

import org.springframework.cloud.openfeign.FallbackFactory;

@Component
public class SmsFeignFallback implements FallbackFactory<SmsFeign> {

	@Override
	public SmsFeign create(final Throwable e) {
		return new SmsFeign() {

			@Override
			public List<Integer> select4PreTimeSend(Integer limit) {
				return Collections.emptyList();// 降级返回空列表
			}

			@Override
			public Void exePreTimeSend(Integer id) {
				return Result.onFallbackError();
			}

			@Override
			public Void send(SendSmsReq sendSmsReq) {
				return Result.onFallbackError();
			}
		};
	}
}
