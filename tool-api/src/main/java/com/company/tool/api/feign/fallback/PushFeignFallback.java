package com.company.tool.api.feign.fallback;

import com.company.common.api.Result;
import com.company.tool.api.feign.PushFeign;
import com.company.tool.api.request.BindDeviceReq;
import com.company.tool.api.request.SendPushReq;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PushFeignFallback implements FallbackFactory<PushFeign> {

	@Override
	public PushFeign create(final Throwable e) {
		return new PushFeign() {

			@Override
			public Result<List<Integer>> select4PreTimeSend(Integer limit) {
				return Result.onFallbackError();
			}

			@Override
			public Result<Void> exePreTimeSend(Integer id) {
				return Result.onFallbackError();
			}

			@Override
			public Result<Void> bindDevice(BindDeviceReq bindDeviceReq) {
				return Result.onFallbackError();
			}

			@Override
			public Result<Void> send(SendPushReq sendPushReq) {
				return Result.onFallbackError();
			}
		};
	}
}
