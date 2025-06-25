package com.company.system.api.feign.fallback;

import java.util.List;

import org.springframework.stereotype.Component;

import com.company.common.api.Result;
import com.company.common.request.RemoveReq;
import com.company.common.response.PageResp;
import com.company.system.api.feign.SysRoleFeign;
import com.company.system.api.request.SysRoleGrantMenuReq;
import com.company.system.api.request.SysRoleReq;
import com.company.system.api.response.SysRoleResp;

import org.springframework.cloud.openfeign.FallbackFactory;

@Component
public class SysRoleFeignFallback implements FallbackFactory<SysRoleFeign> {

	@Override
	public SysRoleFeign create(final Throwable e) {
		return new SysRoleFeign() {

			@Override
			public PageResp<SysRoleResp> page(Long current, Long size, String roleName, String roleKey, Integer roleSort, String dataScope, String status, String roleRemark, String createTimeStart, String createTimeEnd, String updateTimeStart, String updateTimeEnd) {
				return Result.onFallbackError();
			}
			
			@Override
			public List<SysRoleResp> list(String roleName, String roleKey, Integer roleSort, String dataScope, String status, String roleRemark, String createTimeStart, String createTimeEnd, String updateTimeStart, String updateTimeEnd) {
				return Result.onFallbackError();
			}
			
			@Override
			public SysRoleResp query(Integer id) {
				return Result.onFallbackError();
			}

			@Override
			public Boolean save(SysRoleReq sysRoleReq) {
				return Result.onFallbackError();
			}

			@Override
			public Boolean update(SysRoleReq sysRoleReq) {
				return Result.onFallbackError();
			}

			@Override
			public Boolean remove(RemoveReq<Integer> req) {
				return Result.onFallbackError();
			}

			@Override
			public Boolean grantMenu(SysRoleGrantMenuReq req) {
				return Result.onFallbackError();
			}
		};
	}
}
