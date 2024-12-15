package com.company.system.api.feign.fallback;

import java.util.List;

import org.springframework.stereotype.Component;

import com.company.common.api.Result;
import com.company.common.request.RemoveReq;
import com.company.common.response.PageResp;
import com.company.system.api.feign.SysRoleMenuFeign;
import com.company.system.api.request.SysRoleMenuReq;
import com.company.system.api.response.SysRoleMenuResp;

import feign.hystrix.FallbackFactory;

@Component
public class SysRoleMenuFeignFallback implements FallbackFactory<SysRoleMenuFeign> {

	@Override
	public SysRoleMenuFeign create(final Throwable e) {
		return new SysRoleMenuFeign() {

			@Override
			public Result<PageResp<SysRoleMenuResp>> page(Long current, Long size, Integer roleId, Integer menuId, String createTimeStart, String createTimeEnd, String updateTimeStart, String updateTimeEnd) {
				return Result.onFallbackError();
			}
			
			@Override
			public Result<List<SysRoleMenuResp>> list(Integer roleId, Integer menuId, String createTimeStart, String createTimeEnd, String updateTimeStart, String updateTimeEnd) {
				return Result.onFallbackError();
			}
			
			@Override
			public Result<SysRoleMenuResp> query(Integer id) {
				return Result.onFallbackError();
			}

			@Override
			public Result<Boolean> save(SysRoleMenuReq sysRoleMenuReq) {
				return Result.onFallbackError();
			}

			@Override
			public Result<Boolean> update(SysRoleMenuReq sysRoleMenuReq) {
				return Result.onFallbackError();
			}

			@Override
			public Result<Boolean> remove(RemoveReq<Integer> req) {
				return Result.onFallbackError();
			}

		};
	}
}
