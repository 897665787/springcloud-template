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
			public PageResp<SysUserRoleResp> page(Long current, Long size, Integer userId, Integer roleId, String createTimeStart, String createTimeEnd, String updateTimeStart, String updateTimeEnd) {
				return Result.onFallbackError();
			}
			
			@Override
			public List<SysUserRoleResp> list(Integer userId, Integer roleId, String createTimeStart, String createTimeEnd, String updateTimeStart, String updateTimeEnd) {
				return Result.onFallbackError();
			}
			
			@Override
			public SysUserRoleResp query(Integer id) {
				return Result.onFallbackError();
			}

			@Override
			public Set<Integer> listRoleIdByUserId(Integer userId) {
				return Result.onFallbackError();
			}

			@Override
			public Boolean save(SysUserRoleReq sysUserRoleReq) {
				return Result.onFallbackError();
			}

			@Override
			public Boolean update(SysUserRoleReq sysUserRoleReq) {
				return Result.onFallbackError();
			}

			@Override
			public Boolean remove(RemoveReq<Integer> req) {
				return Result.onFallbackError();
			}

			@Override
			public Boolean hasPermission(Integer userId, String permission) {
				return Result.onFallbackError();
			}

		};
	}
}
