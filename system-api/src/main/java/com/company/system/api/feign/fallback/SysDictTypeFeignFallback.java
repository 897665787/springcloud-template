package com.company.system.api.feign.fallback;

import java.util.List;

import org.springframework.stereotype.Component;

import com.company.common.api.Result;
import com.company.common.request.RemoveReq;
import com.company.common.response.PageResp;
import com.company.system.api.feign.SysDictTypeFeign;
import com.company.system.api.request.SysDictTypeReq;
import com.company.system.api.response.SysDictTypeResp;

import feign.hystrix.FallbackFactory;

@Component
public class SysDictTypeFeignFallback implements FallbackFactory<SysDictTypeFeign> {

	@Override
	public SysDictTypeFeign create(final Throwable e) {
		return new SysDictTypeFeign() {

			@Override
			public Result<PageResp<SysDictTypeResp>> page(Long current, Long size, String dictName, String dictType, String status, String dictRemark) {
				return Result.onFallbackError();
			}
			
			@Override
			public Result<List<SysDictTypeResp>> list(String dictName, String dictType, String status, String dictRemark) {
				return Result.onFallbackError();
			}
			
			@Override
			public Result<SysDictTypeResp> query(Integer id) {
				return Result.onFallbackError();
			}

			@Override
			public Result<Boolean> save(SysDictTypeReq sysDictTypeReq) {
				return Result.onFallbackError();
			}

			@Override
			public Result<Boolean> update(SysDictTypeReq sysDictTypeReq) {
				return Result.onFallbackError();
			}

			@Override
			public Result<Boolean> remove(RemoveReq<Integer> req) {
				return Result.onFallbackError();
			}

		};
	}
}
