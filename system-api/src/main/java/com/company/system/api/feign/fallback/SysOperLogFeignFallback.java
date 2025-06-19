package com.company.system.api.feign.fallback;

import java.util.List;

import org.springframework.stereotype.Component;

import com.company.common.api.Result;
import com.company.common.request.RemoveReq;
import com.company.common.response.PageResp;
import com.company.system.api.feign.SysOperLogFeign;
import com.company.system.api.request.SysOperLogReq;
import com.company.system.api.response.SysOperLogResp;

import org.springframework.cloud.openfeign.FallbackFactory;

@Component
public class SysOperLogFeignFallback implements FallbackFactory<SysOperLogFeign> {

	@Override
	public SysOperLogFeign create(final Throwable e) {
		return new SysOperLogFeign() {

			@Override
			public PageResp<SysOperLogResp> page(Long current, Long size, Integer sysUserId, String title, Integer businessType, String method, String requestMethod, String operUrl, String operIp, String operLocation, String operParam, String jsonResult, Integer status, String errorMsg, Integer costTime, String operTimeStart, String operTimeEnd, String createTimeStart, String createTimeEnd, String updateTimeStart, String updateTimeEnd) {
				return Result.onFallbackError();
			}

			@Override
			public List<SysOperLogResp> list(Integer sysUserId, String title, Integer businessType, String method, String requestMethod, String operUrl, String operIp, String operLocation, String operParam, String jsonResult, Integer status, String errorMsg, Integer costTime, String operTimeStart, String operTimeEnd, String createTimeStart, String createTimeEnd, String updateTimeStart, String updateTimeEnd) {
				return Result.onFallbackError();
			}

			@Override
			public SysOperLogResp query(Integer id) {
				return Result.onFallbackError();
			}

			@Override
			public Boolean save(SysOperLogReq sysOperLogReq) {
				return Result.onFallbackError();
			}

			@Override
			public Boolean update(SysOperLogReq sysOperLogReq) {
				return Result.onFallbackError();
			}

			@Override
			public Boolean remove(RemoveReq<Integer> req) {
				return Result.onFallbackError();
			}

		};
	}
}
