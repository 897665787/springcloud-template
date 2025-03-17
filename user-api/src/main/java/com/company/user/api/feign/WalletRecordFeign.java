package com.company.user.api.feign;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.company.common.api.Result;
import com.company.user.api.constant.Constants;
import com.company.user.api.response.WalletRecordResp;

import org.springframework.cloud.openfeign.FallbackFactory;

@FeignClient(value = Constants.FEIGNCLIENT_VALUE, path = "/walletRecord", fallbackFactory = WalletRecordFeign.WalletRecordFeignFactory.class)
public interface WalletRecordFeign {

	@GetMapping("/pageMain")
	Result<List<WalletRecordResp>> pageMain(@RequestParam("current") Integer current, @RequestParam("size") Integer size);

	@Component
	class WalletRecordFeignFactory implements FallbackFactory<WalletRecordFeign> {

		@Override
		public WalletRecordFeign create(Throwable throwable) {
			return new WalletRecordFeign() {

				@Override
				public Result<List<WalletRecordResp>> pageMain(Integer current, Integer size) {
					return Result.onFallbackError();
				}

			};
		}
	}

}
