package com.company.order.api.feign;

import java.util.Map;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.company.common.api.Result;

import feign.hystrix.FallbackFactory;

@FeignClient(value = "template-order", path = "/iosnotify", fallbackFactory = IosNotifyFeign.IosNotifyFeignFactory.class)
public interface IosNotifyFeign {

	/**
	 * 支付回调
	 */
	@PostMapping("/iosPayNotify")
	Result<String> iosPayNotify(@RequestBody Map<String, String> params);
	
	@Component
	class IosNotifyFeignFactory implements FallbackFactory<IosNotifyFeign> {

		@Override
		public IosNotifyFeign create(Throwable throwable) {
			return new IosNotifyFeign() {

				@Override
				public Result<String> iosPayNotify(Map<String, String> params) {
					return Result.onFallbackError();
				}

			};
		}
	}
}
