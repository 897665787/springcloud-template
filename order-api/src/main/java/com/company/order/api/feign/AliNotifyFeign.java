package com.company.order.api.feign;

import java.util.Map;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.company.common.api.Result;

import feign.hystrix.FallbackFactory;

@FeignClient(value = "template-order", path = "/alinotify", fallbackFactory = AliNotifyFeign.AliNotifyFeignFactory.class)
public interface AliNotifyFeign {

	/**
	 * 支付宝支付回调
	 */
	@PostMapping("/aliPayNotify")
	Result<String> aliPayNotify(@RequestBody Map<String, String> params);
	
	@Component
	class AliNotifyFeignFactory implements FallbackFactory<AliNotifyFeign> {

		@Override
		public AliNotifyFeign create(Throwable throwable) {
			return new AliNotifyFeign() {

				@Override
				public Result<String> aliPayNotify(Map<String, String> params) {
					return Result.onFallbackError();
				}

			};
		}
	}
}
