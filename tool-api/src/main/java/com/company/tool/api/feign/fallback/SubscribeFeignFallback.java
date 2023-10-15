package com.company.tool.api.feign.fallback;

import java.util.List;

import org.springframework.stereotype.Component;

import com.company.common.api.Result;
import com.company.common.api.ResultCode;
import com.company.tool.api.feign.SubscribeFeign;
import com.company.tool.api.request.SubscribeGrantReq;
import com.company.tool.api.request.SubscribeSendReq;

import feign.hystrix.FallbackFactory;

@Component
public class SubscribeFeignFallback implements FallbackFactory<SubscribeFeign> {

	@Override
	public SubscribeFeign create(final Throwable e) {
		return new SubscribeFeign() {

			@Override
			public Result<List<String>> selectTemplateCodeByGroup(String group) {
				return Result.fail(ResultCode.FALLBACK);
			}

			@Override
			public Result<Void> grant(SubscribeGrantReq subscribeGrantReq) {
				return Result.fail(ResultCode.FALLBACK);
			}

			@Override
			public Result<Void> send(SubscribeSendReq subscribeSendReq) {
				return Result.fail(ResultCode.FALLBACK);
			}

			@Override
			public Result<List<Integer>> select4PreTimeSend(Integer limit) {
				return Result.fail(ResultCode.FALLBACK);
			}

			@Override
			public Result<Void> exePreTimeSend(Integer id) {
				return Result.fail(ResultCode.FALLBACK);
			}

			@Override
			public Result<Void> syncTemplate() {
				return Result.fail(ResultCode.FALLBACK);
			}

		};
	}
}
