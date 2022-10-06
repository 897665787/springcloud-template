package com.company.order.api.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.company.common.api.Result;
import com.company.order.api.request.PayCloseReq;
import com.company.order.api.request.PayRefundReq;
import com.company.order.api.request.PayReq;
import com.company.order.api.request.RefundReq;
import com.company.order.api.response.PayInfoResp;
import com.company.order.api.response.PayResp;
import com.company.order.api.response.PayTradeStateResp;

import feign.hystrix.FallbackFactory;

@FeignClient(value = "template-order", path = "/pay", fallbackFactory = PayFeign.PayFeignFactory.class)
public interface PayFeign {

	/**
	 * 统一下单
	 * 
	 * @param payReq
	 * @return 支付结果
	 */
	@PostMapping("/unifiedorder")
	Result<PayResp> unifiedorder(@RequestBody PayReq payReq);

	/**
	 * 查询支付信息
	 * 
	 * @param orderCode
	 * @return 支付信息
	 */
	@GetMapping("/queryPayInfo")
	Result<PayInfoResp> queryPayInfo(@RequestParam("orderCode") String orderCode);

	/**
	 * 查询交易状态
	 * 
	 * @param orderCode
	 * @return
	 */
	@GetMapping("/queryTradeState")
	Result<PayTradeStateResp> queryTradeState(@RequestParam("orderCode") String orderCode);

	/**
	 * 退款
	 * 
	 * @param payRefundReq
	 * @return
	 */
	@PostMapping("/refund")
	Result<Void> refund(@RequestBody PayRefundReq payRefundReq);

	/**
	 * 退款带重试机制
	 * @param refundReq
	 * @return
	 */
	@PostMapping("/refundWithRetry")
	Result<Void> refundWithRetry(@RequestBody RefundReq refundReq);

	/**
	 * 关闭订单
	 *
	 * @param payCloseReq
	 * @return
	 */
	@PostMapping("/payClose")
	Result<Void> payClose(@RequestBody PayCloseReq payCloseReq);
	
	@Component
	class PayFeignFactory implements FallbackFactory<PayFeign> {

		@Override
		public PayFeign create(Throwable throwable) {
			return new PayFeign() {

				@Override
				public Result<PayResp> unifiedorder(PayReq payReq) {
					return Result.onFallbackError();
				}

				@Override
				public Result<PayInfoResp> queryPayInfo(String orderCode) {
					return Result.onFallbackError();
				}

				@Override
				public Result<PayTradeStateResp> queryTradeState(String orderCode) {
					return Result.onFallbackError();
				}

				@Override
				public Result<Void> refund(PayRefundReq payRefundReq) {
					return Result.onFallbackError();
				}
				
				@Override
				public Result<Void> refundWithRetry(RefundReq refundReq) {
					return Result.onFallbackError();
				}

				@Override
				public Result<Void> payClose(PayCloseReq payCloseReq) {
					return Result.onFallbackError();
				}

			};
		}
	}
}
