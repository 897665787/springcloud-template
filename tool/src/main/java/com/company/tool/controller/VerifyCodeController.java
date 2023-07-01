package com.company.tool.controller;

import java.time.LocalDateTime;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.company.common.api.Result;
import com.company.tool.api.feign.VerifyCodeFeign;
import com.company.tool.api.response.CaptchaResp;
import com.company.tool.enums.SmsEnum;
import com.company.tool.service.VerifyCodeService;
import com.company.tool.sms.AsyncSmsSender;
import com.google.common.collect.Maps;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.LineCaptcha;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.RandomUtil;

@RestController
@RequestMapping(value = "/verifyCode")
public class VerifyCodeController implements VerifyCodeFeign {
	@Autowired
	private VerifyCodeService verifyCodeService;
	@Autowired
	private AsyncSmsSender asyncSmsSender;
	
	@Override
	public Result<String> sms(String mobile, String type) {
		String code = RandomUtil.randomNumbers(6);

		verifyCodeService.save(type, mobile, code);

		Map<String, String> templateParamMap = Maps.newHashMap();
		templateParamMap.put("code", code);
		LocalDateTime planSendTime = LocalDateTime.now();
		LocalDateTime overTime = planSendTime.plusMinutes(5);

		asyncSmsSender.send(mobile, templateParamMap, SmsEnum.Type.VERIFYCODE, planSendTime, overTime);

		return Result.success("验证码发送成功");
	}

	@Override
	public Result<CaptchaResp> captcha(String type) {
		LineCaptcha lineCaptcha = CaptchaUtil.createLineCaptcha(200, 90, 4, 100);
		String code = lineCaptcha.getCode();
		String uuid = IdUtil.fastSimpleUUID();

		verifyCodeService.save(type, uuid, code);

		CaptchaResp resp = new CaptchaResp();
		resp.setUuid(uuid);
		resp.setImageBase64Data(lineCaptcha.getImageBase64Data());
		return Result.success(resp);
	}

	@Override
	public Result<Boolean> verify(String type, String certificate, String inputcode) {
		boolean verifyPass = verifyCodeService.verify(type, certificate, inputcode);
		return Result.success(verifyPass);
	}
}
