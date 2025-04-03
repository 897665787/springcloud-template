package com.company.order.api.feign;

import java.util.Map;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.company.common.api.Result;
import com.company.order.api.constant.Constants;
import com.company.order.api.response.SpiOrderSendNotifyResp;

import org.springframework.cloud.openfeign.FallbackFactory;

@FeignClient(value = Constants.FEIGNCLIENT_VALUE, path = "/aliactivitynotify", fallbackFactory = AliActivityNotifyFeign.AliActivityNotifyFeignFactory.class)
public interface AliActivityNotifyFeign {

	/**
	 * 支付宝营销活动SPI回调(订单券发放)
	 */
	@PostMapping("/spiOrderSendNotify")
	Result<SpiOrderSendNotifyResp> spiOrderSendNotify(@RequestBody Map<String, String> aliParams);
	/**
	 * 支付宝营销活动FROM回调
	 */
	@PostMapping("/fromNotify")
	Result<String> fromNotify(@RequestBody Map<String, String> params);
	
	@Component
	class AliActivityNotifyFeignFactory implements FallbackFactory<AliActivityNotifyFeign> {

		@Override
		public AliActivityNotifyFeign create(Throwable throwable) {
			return new AliActivityNotifyFeign() {

				@Override
				public Result<String> fromNotify(Map<String, String> params) {
					return Result.onFallbackError();
				}

				@Override
				public Result<SpiOrderSendNotifyResp> spiOrderSendNotify(Map<String, String> aliParams) {
					return Result.onFallbackError();
				}

			};
		}
	}
}
