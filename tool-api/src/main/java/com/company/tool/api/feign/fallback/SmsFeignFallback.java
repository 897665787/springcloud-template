package com.company.tool.api.feign.fallback;

import java.util.List;

import org.springframework.stereotype.Component;

import com.company.common.api.Result;
import com.company.common.api.ResultCode;
import com.company.tool.api.feign.SmsFeign;

import feign.hystrix.FallbackFactory;

@Component
public class SmsFeignFallback implements FallbackFactory<SmsFeign> {

	@Override
	public SmsFeign create(final Throwable e) {
		return new SmsFeign() {

			@Override
			public Result<List<Integer>> select4PreTimeSend(Integer limit) {
				return Result.fail(ResultCode.FALLBACK);
			}

			@Override
			public Result<Void> exePreTimeSend(Integer id) {
				return Result.fail(ResultCode.FALLBACK);
			}
		};
	}
}
