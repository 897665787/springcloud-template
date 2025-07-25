package com.company.system.api.feign.fallback;

import java.util.List;

import org.springframework.stereotype.Component;

import com.company.common.api.Result;
import com.company.system.api.request.RemoveReq;
import com.company.system.api.response.PageResp;
import com.company.system.api.feign.SysLogininfoFeign;
import com.company.system.api.request.SysLogininfoReq;
import com.company.system.api.response.SysLogininfoResp;

import org.springframework.cloud.openfeign.FallbackFactory;

@Component
public class SysLogininfoFeignFallback implements FallbackFactory<SysLogininfoFeign> {

	@Override
	public SysLogininfoFeign create(final Throwable e) {
		return new SysLogininfoFeign() {

			@Override
			public Result<PageResp<SysLogininfoResp>> page(Long current, Long size, Integer sysUserId, String loginTimeStart, String loginTimeEnd, String account, String device, String platform, String operator, String version, String deviceid, String channel, String ip, String address, String source, String lang, String createTimeStart, String createTimeEnd, String updateTimeStart, String updateTimeEnd) {
				return Result.onFallbackError();
			}

			@Override
			public Result<List<SysLogininfoResp>> list(Integer sysUserId, String loginTimeStart, String loginTimeEnd, String account, String device, String platform, String operator, String version, String deviceid, String channel, String ip, String address, String source, String lang, String createTimeStart, String createTimeEnd, String updateTimeStart, String updateTimeEnd) {
				return Result.onFallbackError();
			}

			@Override
			public Result<SysLogininfoResp> query(Integer id) {
				return Result.onFallbackError();
			}

			@Override
			public Result<Boolean> save(SysLogininfoReq sysLogininfoReq) {
				return Result.onFallbackError();
			}

			@Override
			public Result<Boolean> update(SysLogininfoReq sysLogininfoReq) {
				return Result.onFallbackError();
			}

			@Override
			public Result<Boolean> remove(RemoveReq<Integer> req) {
				return Result.onFallbackError();
			}

		};
	}
}
