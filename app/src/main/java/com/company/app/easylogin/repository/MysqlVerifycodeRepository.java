package com.company.app.easylogin.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import com.company.app.constants.Constants;
import com.company.tool.api.feign.VerifyCodeFeign;
import com.jqdi.easylogin.core.repository.VerifycodeRepository;

@Component
@RequiredArgsConstructor
public class MysqlVerifycodeRepository implements VerifycodeRepository {

	private final VerifyCodeFeign verifyCodeFeign;

	@Override
	public boolean checkVerifycode(String identifier, String verifyCode) {
		Boolean verifyPass = verifyCodeFeign.verify(Constants.VerifyCodeType.LOGIN, identifier, verifyCode)
				;
		return verifyPass;
	}

}