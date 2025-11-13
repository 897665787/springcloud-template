package com.company.user.api.feign;

import com.company.common.api.Result;
import com.company.user.api.constant.Constants;
import com.company.user.api.feign.fallback.ThrowExceptionFallback;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(value = Constants.FEIGNCLIENT_VALUE, path = "/walletIncomeUseRecord", fallbackFactory = ThrowExceptionFallback.class)
public interface WalletIncomeUseRecordFeign {

	@GetMapping("/selectId4Expire")
	Result<List<Integer>> selectId4Expire(@RequestParam("limit") Integer limit);

	@PostMapping("/update4Expire")
	Result<Boolean> update4Expire(@RequestParam("id") Integer id);
}
