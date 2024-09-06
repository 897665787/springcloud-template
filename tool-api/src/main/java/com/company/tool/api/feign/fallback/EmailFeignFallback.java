package com.company.tool.api.feign.fallback;

import java.util.List;

import org.springframework.stereotype.Component;

import com.company.common.api.Result;
import com.company.tool.api.feign.EmailFeign;
import com.company.tool.api.request.SendEmailReq;

import feign.hystrix.FallbackFactory;

@Component
public class EmailFeignFallback implements FallbackFactory<EmailFeign> {

	@Override
	public EmailFeign create(final Throwable e) {
		return new EmailFeign() {

			@Override
			public Result<List<Integer>> select4PreTimeSend(Integer limit) {
				return Result.onFallbackError();
			}

			@Override
			public Result<Void> exePreTimeSend(Integer id) {
				return Result.onFallbackError();
			}

			@Override
			public Result<Void> send(SendEmailReq sendEmailReq) {
				return Result.onFallbackError();
			}
		};
	}
}
