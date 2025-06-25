package com.company.system.api.feign.fallback;

import org.springframework.stereotype.Component;

import com.company.common.api.Result;
import com.company.system.api.feign.SysUserPasswordFeign;
import com.company.system.api.request.SaveNewPasswordReq;
import com.company.system.api.response.SysUserPasswordResp;

import org.springframework.cloud.openfeign.FallbackFactory;

@Component
public class SysUserPasswordFeignFallback implements FallbackFactory<SysUserPasswordFeign> {

	@Override
	public SysUserPasswordFeign create(final Throwable e) {
		return new SysUserPasswordFeign() {

			@Override
			public SysUserPasswordResp getBySysUserId(Integer sysUserId) {
				return Result.onFallbackError();
			}

			@Override
			public String getPasswordBySysUserId(Integer sysUserId) {
				return Result.onFallbackError();
			}

			@Override
			public Void saveNewPassword(SaveNewPasswordReq saveNewPasswordReq) {
				return Result.onFallbackError();
			}

		};
	}
}
