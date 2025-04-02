package com.company.system.api.feign.fallback;

import java.util.List;

import org.springframework.stereotype.Component;

import com.company.common.api.Result;
import com.company.common.request.RemoveReq;
import com.company.common.response.PageResp;
import com.company.system.api.feign.SysMenuFeign;
import com.company.system.api.request.SysMenuReq;
import com.company.system.api.response.RouterResp;
import com.company.system.api.response.SysMenuResp;

import org.springframework.cloud.openfeign.FallbackFactory;

@Component
public class SysMenuFeignFallback implements FallbackFactory<SysMenuFeign> {

	@Override
	public SysMenuFeign create(final Throwable e) {
		return new SysMenuFeign() {

			@Override
			public Result<PageResp<SysMenuResp>> page(Long current, Long size, Integer parentId, String menuName, String menuType, String status, Integer visible, String perms, String createTimeStart, String createTimeEnd) {
				return Result.onFallbackError();
			}
			
			@Override
			public Result<List<SysMenuResp>> list(Integer parentId, String menuName, Integer orderNum, String menuType, String status, Integer visible, String perms, String createTimeStart, String createTimeEnd) {
				return Result.onFallbackError();
			}
			
			@Override
			public Result<SysMenuResp> query(Integer id) {
				return Result.onFallbackError();
			}

			@Override
			public Result<Boolean> save(SysMenuReq sysMenuReq) {
				return Result.onFallbackError();
			}

			@Override
			public Result<Boolean> update(SysMenuReq sysMenuReq) {
				return Result.onFallbackError();
			}

			@Override
			public Result<Boolean> remove(RemoveReq<Integer> req) {
				return Result.onFallbackError();
			}

			@Override
			public Result<List<RouterResp>> getRouters(Integer userId) {
				return Result.onFallbackError();
			}
		};
	}
}
