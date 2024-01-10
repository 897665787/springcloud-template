package com.company.order.api.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.company.common.api.Result;
import com.company.order.api.constant.Constants;

import feign.hystrix.FallbackFactory;

@FeignClient(value = Constants.FEIGNCLIENT_VALUE, path = "/wxnotify", fallbackFactory = WxNotifyFeign.WxNotifyFeignFactory.class)
public interface WxNotifyFeign {

	/**
	 * 微信支付回调
	 */
	@PostMapping("/wxPayNotify")
	Result<String> wxPayNotify(@RequestBody String xmlString);
	
	/**
	 * 退款回调
	 * 
	 * @param xmlString
	 * @return
	 */
	@PostMapping("/wxPayRefundNotify")
	Result<String> wxPayRefundNotify(@RequestBody String xmlString);
	
	@Component
	class WxNotifyFeignFactory implements FallbackFactory<WxNotifyFeign> {

		@Override
		public WxNotifyFeign create(Throwable throwable) {
			return new WxNotifyFeign() {

				@Override
				public Result<String> wxPayNotify(String xmlString) {
					return Result.onFallbackError();
				}

				@Override
				public Result<String> wxPayRefundNotify(String xmlString) {
					return Result.onFallbackError();
				}

			};
		}
	}
}
