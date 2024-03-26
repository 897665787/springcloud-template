package com.company.tool.api.feign.fallback;

import java.util.List;

import org.springframework.stereotype.Component;

import com.company.common.api.Result;
import com.company.tool.api.feign.BannerFeign;
import com.company.tool.api.request.BannerReq;
import com.company.tool.api.response.BannerResp;

import feign.hystrix.FallbackFactory;

@Component
public class BannerFeignFallback implements FallbackFactory<BannerFeign> {

	@Override
	public BannerFeign create(final Throwable e) {
		return new BannerFeign() {
			@Override
			public Result<List<BannerResp>> list(BannerReq bannerReq) {
				return Result.onFallbackError();
			}
		};
	}
}
