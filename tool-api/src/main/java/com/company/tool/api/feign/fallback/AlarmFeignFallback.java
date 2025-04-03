package com.company.tool.api.feign.fallback;

import org.springframework.stereotype.Component;

import com.company.common.api.Result;
import com.company.tool.api.feign.AlarmFeign;
import com.company.tool.api.request.AlarmReq;
import com.company.tool.api.request.WebhookAlarmReq;

import org.springframework.cloud.openfeign.FallbackFactory;

@Component
public class AlarmFeignFallback implements FallbackFactory<AlarmFeign> {

	@Override
	public AlarmFeign create(final Throwable e) {
		return new AlarmFeign() {

			@Override
			public Result<Void> webhook(WebhookAlarmReq webhookAlarmReq) {
				return Result.onFallbackError();
			}

			@Override
			public Result<Void> sms(AlarmReq alarmReq) {
				return Result.onFallbackError();
			}

			@Override
			public Result<Void> email(AlarmReq alarmReq) {
				return Result.onFallbackError();
			}

			@Override
			public Result<Void> warn(AlarmReq alarmReq) {
				return Result.onFallbackError();
			}
		};
	}
}
