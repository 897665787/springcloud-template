package com.company.tool.api.feign.fallback;

import java.util.List;

import org.springframework.stereotype.Component;

import com.company.common.api.Result;
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
				return Result.onFallbackError();
			}

			@Override
			public Result<Void> grant(SubscribeGrantReq subscribeGrantReq) {
				return Result.onFallbackError();
			}

			@Override
			public Result<Void> send(SubscribeSendReq subscribeSendReq) {
				return Result.onFallbackError();
			}

			@Override
			public Result<List<Integer>> select4PreTimeSend(Integer limit) {
				return Result.onFallbackError();
			}

			@Override
			public Result<Void> exePreTimeSend(Integer id) {
				return Result.onFallbackError();
			}

			@Override
			public Result<Void> syncTemplate() {
				return Result.onFallbackError();
			}

		};
	}
}
