package com.company.tool.api.feign;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.company.common.api.Result;
import com.company.tool.api.constant.Constants;
import com.company.tool.api.feign.RetryerFeign.RetryerFeignFactory;
import com.company.tool.api.request.RetryerInfoReq;

import feign.hystrix.FallbackFactory;

@FeignClient(value = Constants.FEIGNCLIENT_VALUE, path = "/retryer", fallbackFactory = RetryerFeignFactory.class)
public interface RetryerFeign {
	@PostMapping("/call")
	Result<Void> call(@RequestBody RetryerInfoReq req);

	@GetMapping("/selectId4Call")
	Result<List<Integer>> selectId4Call();

	@PostMapping("/callById")
	Result<Void> callById(@RequestParam("id") Integer id);

	@Component
	class RetryerFeignFactory implements FallbackFactory<RetryerFeign> {

		@Override
		public RetryerFeign create(Throwable throwable) {
			return new RetryerFeign() {
				@Override
				public Result<Void> call(RetryerInfoReq req) {
					return Result.onFallbackError();
				}

				@Override
				public Result<List<Integer>> selectId4Call() {
					return Result.onFallbackError();
				}

				@Override
				public Result<Void> callById(Integer id) {
					return Result.onFallbackError();
				}

			};
		}
	}

}
