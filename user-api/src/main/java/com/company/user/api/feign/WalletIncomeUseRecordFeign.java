package com.company.user.api.feign;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.company.common.api.Result;
import com.company.user.api.constant.Constants;

import feign.hystrix.FallbackFactory;

@FeignClient(value = Constants.FEIGNCLIENT_VALUE, path = "/walletIncomeUseRecord", fallbackFactory = WalletIncomeUseRecordFeign.WalletIncomeUseRecordFeignFactory.class)
public interface WalletIncomeUseRecordFeign {

	@GetMapping("/selectId4Expire")
	Result<List<Integer>> selectId4Expire(@RequestParam("limit") Integer limit);

	@PostMapping("/update4Expire")
	Result<Boolean> update4Expire(@RequestParam("id") Integer id);

	@Component
	class WalletIncomeUseRecordFeignFactory implements FallbackFactory<WalletIncomeUseRecordFeign> {

		@Override
		public WalletIncomeUseRecordFeign create(Throwable throwable) {
			return new WalletIncomeUseRecordFeign() {

				@Override
				public Result<List<Integer>> selectId4Expire(Integer limit) {
					return Result.onFallbackError();
				}

				@Override
				public Result<Boolean> update4Expire(Integer id) {
					return Result.onFallbackError();
				}

			};
		}
	}

}
