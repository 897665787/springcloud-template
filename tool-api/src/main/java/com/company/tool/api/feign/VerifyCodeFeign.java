package com.company.tool.api.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.company.common.api.Result;
import com.company.tool.api.feign.fallback.VerifyCodeFeignFallback;
import com.company.tool.api.response.CaptchaResp;

@FeignClient(value = "template-tool", path = "/verifyCode", fallbackFactory = VerifyCodeFeignFallback.class)
public interface VerifyCodeFeign {

	@PostMapping("/sms")
	Result<String> sms(@RequestParam("mobile") String mobile, @RequestParam("type") String type);

	@PostMapping("/captcha")
	Result<CaptchaResp> captcha(@RequestParam("type") String type);
}
