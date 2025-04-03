package com.company.system.api.feign.fallback;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.company.common.api.Result;
import com.company.common.request.RemoveReq;
import com.company.common.response.PageResp;
import com.company.system.api.feign.SysUserFeign;
import com.company.system.api.request.SysUserAssignRoleReq;
import com.company.system.api.request.SysUserReq;
import com.company.system.api.response.SysUserInfoResp;
import com.company.system.api.response.SysUserResp;

import org.springframework.cloud.openfeign.FallbackFactory;

@Component
public class SysUserFeignFallback implements FallbackFactory<SysUserFeign> {

	@Override
	public SysUserFeign create(final Throwable e) {
		return new SysUserFeign() {

			@Override
			public Result<PageResp<SysUserResp>> page(Long current, Long size, String account, String nickname, String email, String phonenumber, String sex, String avatar, String status, Integer deptId, String userRemark, String createTimeStart, String createTimeEnd, String updateTimeStart, String updateTimeEnd) {
				return Result.onFallbackError();
			}
			
			@Override
			public Result<List<SysUserResp>> list(String account, String nickname, String email, String phonenumber, String sex, String avatar, String status, Integer deptId, String userRemark, String createTimeStart, String createTimeEnd, String updateTimeStart, String updateTimeEnd) {
				return Result.onFallbackError();
			}
			
			@Override
			public Result<SysUserResp> query(Integer id) {
				return Result.onFallbackError();
			}

			@Override
			public Result<Boolean> save(SysUserReq sysUserReq) {
				return Result.onFallbackError();
			}

			@Override
			public Result<Boolean> update(SysUserReq sysUserReq) {
				return Result.onFallbackError();
			}

			@Override
			public Result<Boolean> remove(RemoveReq<Integer> req) {
				return Result.onFallbackError();
			}

			@Override
			public Result<SysUserResp> getByAccount(String account) {
				return Result.onFallbackError();
			}

			@Override
			public Result<SysUserResp> getById(Integer id) {
				return Result.onFallbackError();
			}

			@Override
			public Result<SysUserInfoResp> getInfo(Integer userId) {
				return Result.onFallbackError();
			}

			@Override
			public Result<List<SysUserResp>> getByBatchId(List<Integer> ids) {
				return Result.onFallbackError();
			}

			@Override
			public Result<Map<Integer, String>> mapNicknameById(Collection<Integer> ids) {
				return Result.onFallbackError();
			}

			@Override
			public Result<Boolean> assignRole(SysUserAssignRoleReq req) {
				return Result.onFallbackError();
			}
		};
	}
}
