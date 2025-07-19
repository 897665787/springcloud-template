package com.company.order.api.feign;

import java.util.Map;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.company.common.api.Result;
import com.company.order.api.constant.Constants;

import org.springframework.cloud.openfeign.FallbackFactory;

@FeignClient(value = Constants.FEIGNCLIENT_VALUE, path = "/iosnotify", fallbackFactory = IosNotifyFeign.IosNotifyFeignFactory.class)
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
