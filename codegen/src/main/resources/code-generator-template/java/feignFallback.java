package com.company.{module}.api.feign.fallback;

import java.util.List;
import java.math.BigDecimal;

import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

import com.company.common.request.RemoveReq;
import com.company.common.api.Result;
import com.company.common.response.PageResp;
import com.company.{module}.api.feign.{ModelName}Feign;
import com.company.{module}.api.request.{ModelName}Req;
import com.company.{module}.api.response.{ModelName}Resp;

@Component
public class {ModelName}FeignFallback implements FallbackFactory<{ModelName}Feign> {

	@Override
	public {ModelName}Feign create(final Throwable e) {
		return new {ModelName}Feign() {

			@Override
			public Result<PageResp<{ModelName}Resp>> page({page_column_field_feign}) {
				return Result.onFallbackError();
			}
			
			@Override
			public Result<List<{ModelName}Resp>> list({column_field_feign}) {
				return Result.onFallbackError();
			}
			
			@Override
			public Result<{ModelName}Resp> query(Integer id) {
				return Result.onFallbackError();
			}

			@Override
			public Result<Boolean> save({ModelName}Req {modelName}Req) {
				return Result.onFallbackError();
			}

			@Override
			public Result<Boolean> update({ModelName}Req {modelName}Req) {
				return Result.onFallbackError();
			}

			@Override
			public Result<Boolean> remove(RemoveReq<Integer> req) {
				return Result.onFallbackError();
			}

		};
	}
}
