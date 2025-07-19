package com.company.system.api.feign.fallback;

import java.util.List;

import org.springframework.stereotype.Component;

import com.company.common.api.Result;
import com.company.system.api.request.RemoveReq;
import com.company.system.api.response.PageResp;
import com.company.system.api.feign.SysConfigFeign;
import com.company.system.api.request.SysConfigReq;
import com.company.system.api.response.SysConfigResp;

import org.springframework.cloud.openfeign.FallbackFactory;

@Component
public class SysConfigFeignFallback implements FallbackFactory<SysConfigFeign> {

	@Override
	public SysConfigFeign create(final Throwable e) {
		return new SysConfigFeign() {

			@Override
			public Result<PageResp<SysConfigResp>> page(Long current, Long size, String name, String code, String value, String configRemark) {
				return Result.onFallbackError();
			}

			@Override
			public Result<List<SysConfigResp>> list(String name, String code, String value, String configRemark) {
				return Result.onFallbackError();
			}

			@Override
			public Result<SysConfigResp> query(Integer id) {
				return Result.onFallbackError();
			}

			@Override
			public Result<Boolean> save(SysConfigReq sysConfigReq) {
				return Result.onFallbackError();
			}

			@Override
			public Result<Boolean> update(SysConfigReq sysConfigReq) {
				return Result.onFallbackError();
			}

			@Override
			public Result<Boolean> remove(RemoveReq<Integer> req) {
				return Result.onFallbackError();
			}

			@Override
			public Result<String> getValueByCode(String code) {
				return Result.onFallbackError();
			}

			@Override
			public Result<Boolean> updateValueByCode(String value, String code) {
				return Result.onFallbackError();
			}

		};
	}
}
