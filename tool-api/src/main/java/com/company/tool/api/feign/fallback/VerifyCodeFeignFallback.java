package com.company.tool.api.feign.fallback;

import org.springframework.stereotype.Component;

import com.company.common.api.Result;
import com.company.tool.api.feign.VerifyCodeFeign;
import com.company.tool.api.response.CaptchaResp;

import org.springframework.cloud.openfeign.FallbackFactory;

@Component
public class VerifyCodeFeignFallback implements FallbackFactory<VerifyCodeFeign> {

	@Override
	public VerifyCodeFeign create(final Throwable e) {
		return new VerifyCodeFeign() {

			@Override
			public String sms(String mobile, String type) {
				return Result.onFallbackError();
			}

			@Override
			public String email(String email, String type) {
				return Result.onFallbackError();
			}

			@Override
			public CaptchaResp captcha(String type) {
				return Result.onFallbackError();
			}

			@Override
			public Boolean verify(String type, String certificate, String inputcode) {
				return Result.onFallbackError();
			}
		};
	}
}
