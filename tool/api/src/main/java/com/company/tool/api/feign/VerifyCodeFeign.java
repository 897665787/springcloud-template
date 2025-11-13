package com.company.tool.api.feign;

import com.company.common.api.Result;
import com.company.tool.api.constant.Constants;
import com.company.tool.api.feign.fallback.ThrowExceptionFallback;
import com.company.tool.api.response.CaptchaResp;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = Constants.FEIGNCLIENT_VALUE, path = "/verifyCode", fallbackFactory = ThrowExceptionFallback.class)
public interface VerifyCodeFeign {

	/**
	 * 短信验证码
	 * 
	 * @param mobile
	 * @param type
	 * @return
	 */
	@PostMapping("/sms")
	Result<String> sms(@RequestParam("mobile") String mobile, @RequestParam("type") String type);
	
	/**
	 * 邮件验证码
	 * 
	 * @param email
	 * @param type
	 * @return
	 */
	@PostMapping("/email")
	Result<String> email(@RequestParam("email") String email, @RequestParam("type") String type);
	
	/**
	 * 图形验证码
	 * 
	 * @param type
	 * @return
	 */
	@PostMapping("/captcha")
	Result<CaptchaResp> captcha(@RequestParam("type") String type);

	/**
	 * 验证
	 * 
	 * @param type
	 * @param certificate
	 * @param inputcode
	 * @return
	 */
	@GetMapping("/verify")
	Result<Boolean> verify(@RequestParam("type") String type, @RequestParam("certificate") String certificate,
			@RequestParam("inputcode") String inputcode);
}
