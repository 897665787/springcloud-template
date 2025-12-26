package com.company.tool.api.feign.fallback;

import java.util.ArrayList;
import java.util.List;

import com.company.common.api.Result;
import org.springframework.stereotype.Component;


import com.company.tool.api.feign.WebhookFeign;

import org.springframework.cloud.openfeign.FallbackFactory;

@Component
public class WebhookFeignFallback implements FallbackFactory<WebhookFeign> {

	@Override
	public WebhookFeign create(final Throwable e) {
		return new WebhookFeign() {

			@Override
			public List<Integer> select4PreTimeSend(Integer limit) {
				return new ArrayList<>();// 降级返回空列表
			}

			@Override
			public Void exePreTimeSend(Integer id) {
				return Result.onFallbackError();
			}
		};
	}
}
