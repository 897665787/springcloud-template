package com.company.order.api.feign;

import java.util.Map;

import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.company.common.api.Result;
import com.company.order.api.constant.Constants;

@FeignClient(value = Constants.FEIGNCLIENT_VALUE, path = "/alinotify", fallbackFactory = AliNotifyFeign.AliNotifyFeignFactory.class)
public interface AliNotifyFeign {

	/**
	 * 支付宝支付回调
	 */
	@PostMapping("/aliPayNotify")
	String aliPayNotify(@RequestBody Map<String, String> params);

	@Component
	class AliNotifyFeignFactory implements FallbackFactory<AliNotifyFeign> {

		@Override
		public AliNotifyFeign create(Throwable throwable) {
			return new AliNotifyFeign() {

				@Override
				public String aliPayNotify(Map<String, String> params) {
					return Result.onFallbackError();
				}

			};
		}
	}
}
