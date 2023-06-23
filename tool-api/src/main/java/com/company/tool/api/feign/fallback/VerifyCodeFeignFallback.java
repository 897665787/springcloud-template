package com.company.tool.api.feign.fallback;

import org.springframework.stereotype.Component;

import com.company.common.api.Result;
import com.company.common.api.ResultCode;
import com.company.tool.api.feign.VerifyCodeFeign;
import com.company.tool.api.response.CaptchaResp;

import feign.hystrix.FallbackFactory;

@Component
public class VerifyCodeFeignFallback implements FallbackFactory<VerifyCodeFeign> {

	@Override
	public VerifyCodeFeign create(final Throwable e) {
		return new VerifyCodeFeign() {

			@Override
			public Result<String> sms(String mobile, String type) {
				return Result.fail(ResultCode.FALLBACK);
			}

			@Override
			public Result<CaptchaResp> captcha(String type) {
				return Result.fail(ResultCode.FALLBACK);
			}

			@Override
			public Result<Boolean> verify(String type, String certificate, String inputcode) {
				return Result.fail(ResultCode.FALLBACK);
			}
		};
	}
}
