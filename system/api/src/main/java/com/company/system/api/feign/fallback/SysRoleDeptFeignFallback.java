package com.company.system.api.feign.fallback;

import java.util.List;

import org.springframework.stereotype.Component;

import com.company.common.api.Result;
import com.company.system.api.request.RemoveReq;
import com.company.system.api.response.PageResp;
import com.company.system.api.feign.SysRoleDeptFeign;
import com.company.system.api.request.SysRoleDeptReq;
import com.company.system.api.response.SysRoleDeptResp;

import org.springframework.cloud.openfeign.FallbackFactory;

@Component
public class SysRoleDeptFeignFallback implements FallbackFactory<SysRoleDeptFeign> {

	@Override
	public SysRoleDeptFeign create(final Throwable e) {
		return new SysRoleDeptFeign() {

			@Override
			public Result<PageResp<SysRoleDeptResp>> page(Long current, Long size, Integer roleId, Integer deptId) {
				return Result.onFallbackError();
			}

			@Override
			public Result<List<SysRoleDeptResp>> list(Integer roleId, Integer deptId) {
				return Result.onFallbackError();
			}

			@Override
			public Result<SysRoleDeptResp> query(Integer id) {
				return Result.onFallbackError();
			}

			@Override
			public Result<Boolean> save(SysRoleDeptReq sysRoleDeptReq) {
				return Result.onFallbackError();
			}

			@Override
			public Result<Boolean> update(SysRoleDeptReq sysRoleDeptReq) {
				return Result.onFallbackError();
			}

			@Override
			public Result<Boolean> remove(RemoveReq<Integer> req) {
				return Result.onFallbackError();
			}

		};
	}
}
