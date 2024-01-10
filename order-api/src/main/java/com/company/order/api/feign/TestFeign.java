package com.company.order.api.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.company.common.api.Result;
import com.company.order.api.constant.Constants;
import com.company.order.api.feign.TestFeign.TestFeignFactory;
import com.company.order.api.request.PayNotifyReq;
import com.company.order.api.request.RefundNotifyReq;
import com.company.order.api.response.PayInfoResp;
import com.company.order.api.response.PayResp;

import feign.hystrix.FallbackFactory;

@FeignClient(value = Constants.FEIGNCLIENT_VALUE, path = "/test", fallbackFactory = TestFeignFactory.class)
public interface TestFeign {

	@GetMapping("/minipay")
	Result<PayResp> minipay(@RequestParam("orderCode") String orderCode);

	@GetMapping("/apppay")
	Result<PayResp> apppay(@RequestParam("orderCode") String orderCode);

	@GetMapping("/gzhpay")
	Result<PayResp> gzhpay(@RequestParam("orderCode") String orderCode);

	@GetMapping("/mwebpay")
	Result<PayResp> mwebpay(@RequestParam("orderCode") String orderCode);

	@GetMapping("/alipay")
	Result<PayResp> alipay(@RequestParam("orderCode") String orderCode);
	
	@GetMapping("/aliactivitypay")
	Result<PayResp> aliactivitypay(@RequestParam("orderCode") String orderCode);

	@PostMapping("/buyNotify")
	Result<Void> buyNotify(@RequestBody PayNotifyReq payNotifyReq);

	@GetMapping("/queryPayInfo")
	Result<PayInfoResp> queryPayInfo(@RequestParam("orderCode") String orderCode);

	@GetMapping("/refund")
	Result<PayInfoResp> refund(@RequestParam("orderCode") String orderCode,
			@RequestParam("refundOrderCode") String refundOrderCode);

	@PostMapping("/refundNotify")
	Result<Void> refundNotify(@RequestBody RefundNotifyReq refundNotifyReq);
	
	@Component
	class TestFeignFactory implements FallbackFactory<TestFeign> {

		@Override
		public TestFeign create(Throwable throwable) {
			return new TestFeign() {

				@Override
				public Result<Void> buyNotify(PayNotifyReq payNotifyReq) {
					return Result.onFallbackError();
				}

				@Override
				public Result<PayResp> minipay(String orderCode) {

					return Result.onFallbackError();
				}

				@Override
				public Result<PayResp> apppay(String orderCode) {

					return Result.onFallbackError();
				}

				@Override
				public Result<PayResp> gzhpay(String orderCode) {

					return Result.onFallbackError();
				}

				@Override
				public Result<PayResp> mwebpay(String orderCode) {

					return Result.onFallbackError();
				}

				@Override
				public Result<PayResp> alipay(String orderCode) {

					return Result.onFallbackError();
				}
				
				@Override
				public Result<PayResp> aliactivitypay(String orderCode) {
					
					return Result.onFallbackError();
				}

				@Override
				public Result<PayInfoResp> queryPayInfo(String orderCode) {

					return Result.onFallbackError();
				}

				@Override
				public Result<PayInfoResp> refund(String orderCode, String refundOrderCode) {

					return Result.onFallbackError();
				}

				@Override
				public Result<Void> refundNotify(RefundNotifyReq refundNotifyReq) {

					return Result.onFallbackError();
				}

			};
		}
	}
	
}
