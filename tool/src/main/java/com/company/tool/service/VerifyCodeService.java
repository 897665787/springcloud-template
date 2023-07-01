package com.company.tool.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.company.common.exception.BusinessException;
import com.company.tool.entity.VerifyCode;
import com.company.tool.enums.VerifyCodeEnum;
import com.company.tool.mapper.VerifyCodeMapper;

@Service
public class VerifyCodeService extends ServiceImpl<VerifyCodeMapper, VerifyCode> {

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
		LocalDateTime validTime = verifyCode.getValidTime();
		if (validTime.compareTo(LocalDateTime.now()) < 0) {
			throw new BusinessException("验证码已失效");
		}
		Integer maxErrCount = verifyCode.getMaxErrCount();
		Integer errCount = verifyCode.getErrCount();
		if (errCount < maxErrCount) {
			throw new BusinessException("失败次数过多，验证码已失效");
		}
		String code = verifyCode.getCode();
		if (!code.equals(inputcode)) {
			baseMapper.incrErrCount(verifyCode.getId());
			throw new BusinessException("验证码不正确");
		}
		baseMapper.updateStatus(verifyCode.getId(), VerifyCodeEnum.Status.USED);
		return true;
	}
}
