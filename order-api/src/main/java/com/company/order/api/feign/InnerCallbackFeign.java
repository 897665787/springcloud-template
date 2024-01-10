package com.company.order.api.feign;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.company.common.api.Result;
import com.company.order.api.constant.Constants;
import com.company.order.api.feign.InnerCallbackFeign.InnerCallbackFeignFactory;

import feign.hystrix.FallbackFactory;

@FeignClient(value = Constants.FEIGNCLIENT_VALUE, path = "/innerCallback", fallbackFactory = InnerCallbackFeignFactory.class)
public interface InnerCallbackFeign {

	@PostMapping("/postRestTemplate")
	Result<Boolean> postRestTemplate(@RequestParam("id") Integer id);

	@GetMapping("/selectId4CallbackFail")
	Result<List<Integer>> selectId4CallbackFail();

	@Component("orderInnerCallbackFeignFactory")
	class InnerCallbackFeignFactory implements FallbackFactory<InnerCallbackFeign> {

		@Override
		public InnerCallbackFeign create(Throwable throwable) {
			return new InnerCallbackFeign() {
				@Override
				public Result<Boolean> postRestTemplate(Integer id) {
					return Result.onFallbackError();
				}

				@Override
				public Result<List<Integer>> selectId4CallbackFail() {
					return Result.onFallbackError();
				}
			};
		}
	}
}
