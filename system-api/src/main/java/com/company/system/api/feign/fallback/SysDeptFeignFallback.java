package com.company.system.api.feign.fallback;

import java.util.List;

import org.springframework.stereotype.Component;

import com.company.common.api.Result;
import com.company.common.request.RemoveReq;
import com.company.common.response.PageResp;
import com.company.system.api.feign.SysDeptFeign;
import com.company.system.api.request.SysDeptReq;
import com.company.system.api.response.SysDeptResp;

import org.springframework.cloud.openfeign.FallbackFactory;

@Component
public class SysDeptFeignFallback implements FallbackFactory<SysDeptFeign> {

	@Override
	public SysDeptFeign create(final Throwable e) {
		return new SysDeptFeign() {

			@Override
			public PageResp<SysDeptResp> page(Long current, Long size, Integer parentId, String parentIds, String name, Integer orderNum, String status) {
				return Result.onFallbackError();
			}
			
			@Override
			public List<SysDeptResp> list(Integer parentId, String parentIds, String name, Integer orderNum, String status) {
				return Result.onFallbackError();
			}
			
			@Override
			public SysDeptResp query(Integer id) {
				return Result.onFallbackError();
			}

			@Override
			public Boolean save(SysDeptReq sysDeptReq) {
				return Result.onFallbackError();
			}

			@Override
			public Boolean update(SysDeptReq sysDeptReq) {
				return Result.onFallbackError();
			}

			@Override
			public Boolean remove(RemoveReq<Integer> req) {
				return Result.onFallbackError();
			}

		};
	}
}
