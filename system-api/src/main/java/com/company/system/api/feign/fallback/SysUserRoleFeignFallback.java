package com.company.system.api.feign.fallback;

import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Component;

import com.company.common.api.Result;
import com.company.common.request.RemoveReq;
import com.company.common.response.PageResp;
import com.company.system.api.feign.SysUserRoleFeign;
import com.company.system.api.request.SysUserRoleReq;
import com.company.system.api.response.SysUserRoleResp;

import org.springframework.cloud.openfeign.FallbackFactory;

@Component
public class SysUserRoleFeignFallback implements FallbackFactory<SysUserRoleFeign> {

	@Override
	public SysUserRoleFeign create(final Throwable e) {
		return new SysUserRoleFeign() {

			@Override
			public Result<PageResp<SysUserRoleResp>> page(Long current, Long size, Integer userId, Integer roleId, String createTimeStart, String createTimeEnd, String updateTimeStart, String updateTimeEnd) {
				return Result.onFallbackError();
			}
			
			@Override
			public Result<List<SysUserRoleResp>> list(Integer userId, Integer roleId, String createTimeStart, String createTimeEnd, String updateTimeStart, String updateTimeEnd) {
				return Result.onFallbackError();
			}
			
			@Override
			public Result<SysUserRoleResp> query(Integer id) {
				return Result.onFallbackError();
			}

			@Override
			public Result<Set<Integer>> listRoleIdByUserId(Integer userId) {
				return Result.onFallbackError();
			}

			@Override
			public Result<Boolean> save(SysUserRoleReq sysUserRoleReq) {
				return Result.onFallbackError();
			}

			@Override
			public Result<Boolean> update(SysUserRoleReq sysUserRoleReq) {
				return Result.onFallbackError();
			}

			@Override
			public Result<Boolean> remove(RemoveReq<Integer> req) {
				return Result.onFallbackError();
			}

			@Override
			public Result<Boolean> hasPermission(Integer userId, String permission) {
				return Result.onFallbackError();
			}

		};
	}
}
