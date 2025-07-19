package com.company.tool.api.feign.fallback;

import java.util.List;

import org.springframework.stereotype.Component;

import com.company.common.api.Result;
import com.company.tool.api.feign.WebhookFeign;

import org.springframework.cloud.openfeign.FallbackFactory;

@Component
public class WebhookFeignFallback implements FallbackFactory<WebhookFeign> {

	@Override
	public WebhookFeign create(final Throwable e) {
		return new WebhookFeign() {

			@Override
			public Result<List<Integer>> select4PreTimeSend(Integer limit) {
				return Result.onFallbackError();
			}

			@Override
			public Result<Void> exePreTimeSend(Integer id) {
				return Result.onFallbackError();
			}
		};
	}
}
