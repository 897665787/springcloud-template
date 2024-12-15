package com.company.tool.service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.company.common.exception.BusinessException;
import com.company.tool.entity.VerifyCode;
import com.company.tool.enums.VerifyCodeEnum;
import com.company.tool.mapper.VerifyCodeMapper;

import cn.hutool.core.date.LocalDateTimeUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class VerifyCodeService extends ServiceImpl<VerifyCodeMapper, VerifyCode> {
	
	@Value("${template.enable.closeVerify:false}")
	private Boolean closeVerify;// 关闭验证码校验
	
	public VerifyCode selectLastByCertificateType(String certificate, String type) {
		return baseMapper.selectLastByCertificateType(certificate, type);
	}
	
	public void save(String type, String certificate, String code) {
		VerifyCode verifyCode = new VerifyCode();
		verifyCode.setType(type);
		verifyCode.setCertificate(certificate);
		verifyCode.setCode(code);

		LocalDateTime validTime = LocalDateTime.now().plusMinutes(1);
		verifyCode.setValidTime(validTime);
		verifyCode.setStatus(VerifyCodeEnum.Status.UN_USE.getCode());
		verifyCode.setMaxErrCount(5);
		verifyCode.setErrCount(0);
		baseMapper.insert(verifyCode);
	}

	public boolean verify(String type, String certificate, String inputcode) {
		VerifyCode verifyCode = baseMapper.selectLastByCertificateType(certificate, type);
		if (verifyCode == null) {
			throw new BusinessException("验证码不正确");
		}
		if (VerifyCodeEnum.Status.UN_USE != VerifyCodeEnum.Status.of(verifyCode.getStatus())) {
			throw new BusinessException("验证码不可用，请重新获取");
		}
		LocalDateTime validTime = verifyCode.getValidTime();
		LocalDateTime now = LocalDateTime.now();
		if (validTime.compareTo(now) < 0) {
			String message = "验证码已失效";
			long minutes = LocalDateTimeUtil.between(validTime, now, ChronoUnit.MINUTES);
			if (minutes > 10) {// 过时10分钟以上提示不同的提示语
				message = "请获取验证码";
			}
			throw new BusinessException(message);
		}
		Integer maxErrCount = verifyCode.getMaxErrCount();
		Integer errCount = verifyCode.getErrCount();
		if (errCount > maxErrCount) {
			throw new BusinessException("失败次数过多，验证码已失效");
		}
		String code = verifyCode.getCode();
		if (!closeVerify && !code.equalsIgnoreCase(inputcode)) {
			log.info("closeVerify:{}", closeVerify);
			baseMapper.incrErrCount(verifyCode.getId());
			throw new BusinessException("验证码错误");
		}
		baseMapper.updateStatus(verifyCode.getId(), VerifyCodeEnum.Status.USED);
		return true;
	}
}
