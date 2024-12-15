package com.company.system.api.feign.fallback;

import java.util.List;

import org.springframework.stereotype.Component;

import com.company.common.api.Result;
import com.company.common.request.RemoveReq;
import com.company.common.response.PageResp;
import com.company.system.api.feign.SysDictDataFeign;
import com.company.system.api.request.SysDictDataReq;
import com.company.system.api.response.SysDictDataResp;

import feign.hystrix.FallbackFactory;

@Component
public class SysDictDataFeignFallback implements FallbackFactory<SysDictDataFeign> {

	@Override
	public SysDictDataFeign create(final Throwable e) {
		return new SysDictDataFeign() {

			@Override
			public Result<PageResp<SysDictDataResp>> page(Long current, Long size, String dictType, String dictCode, String dictValue, Integer dictSort, String isDefault, String status, String dictRemark) {
				return Result.onFallbackError();
			}
			
			@Override
			public Result<List<SysDictDataResp>> list(String dictType, String dictCode, String dictValue, Integer dictSort, String isDefault, String status, String dictRemark) {
				return Result.onFallbackError();
			}
			
			@Override
			public Result<SysDictDataResp> query(Integer id) {
				return Result.onFallbackError();
			}

			@Override
			public Result<Boolean> save(SysDictDataReq sysDictDataReq) {
				return Result.onFallbackError();
			}

			@Override
			public Result<Boolean> update(SysDictDataReq sysDictDataReq) {
				return Result.onFallbackError();
			}

			@Override
			public Result<Boolean> remove(RemoveReq<Integer> req) {
				return Result.onFallbackError();
			}

			@Override
			public Result<List<SysDictDataResp>> getByType(String type) {
				return Result.onFallbackError();
			}

			@Override
			public Result<String> getValueByTypeCode(String type, String code) {
				return Result.onFallbackError();
			}

		};
	}
}
