package com.company.tool.api.feign.fallback;

import java.util.List;

import org.springframework.stereotype.Component;

import com.company.common.api.Result;
import com.company.tool.api.feign.SmsFeign;
import com.company.tool.api.request.SendSmsReq;

import org.springframework.cloud.openfeign.FallbackFactory;

@Component
public class SmsFeignFallback implements FallbackFactory<SmsFeign> {

	@Override
	public SmsFeign create(final Throwable e) {
		return new SmsFeign() {

			@Override
			public Result<List<Integer>> select4PreTimeSend(Integer limit) {
				return Result.onFallbackError();
			}

			@Override
			public Result<Void> exePreTimeSend(Integer id) {
				return Result.onFallbackError();
			}

			@Override
			public Result<Void> send(SendSmsReq sendSmsReq) {
				return Result.onFallbackError();
			}
		};
	}
}
