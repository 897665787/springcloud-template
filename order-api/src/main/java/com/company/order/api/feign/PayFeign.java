package com.company.order.api.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.company.common.api.Result;
import com.company.order.api.constant.Constants;
import com.company.order.api.request.PayCloseReq;
import com.company.order.api.request.PayRefundReq;
import com.company.order.api.request.PayReq;
import com.company.order.api.request.ToPayReq;
import com.company.order.api.response.PayResp;

import feign.hystrix.FallbackFactory;

@FeignClient(value = Constants.FEIGNCLIENT_VALUE, path = "/pay", fallbackFactory = PayFeign.PayFeignFactory.class)
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
	 * 关闭订单
	 *
	 * @param payCloseReq
	 * @return
	 */
	@PostMapping("/payClose")
	Result<Void> payClose(@RequestBody PayCloseReq payCloseReq);
	
	/**
	 * 去支付（可切换支付方式）
	 * 
	 * @param toPayReq
	 * @return 支付结果
	 */
	@PostMapping("/toPay")
	Result<PayResp> toPay(@RequestBody ToPayReq toPayReq);

	/**
	 * 退款
	 * 
	 * @param payRefundReq
	 * @return
	 */
	@PostMapping("/refund")
	Result<Void> refund(@RequestBody PayRefundReq payRefundReq);
	
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
				public Result<Void> payClose(PayCloseReq payCloseReq) {
					return Result.onFallbackError();
				}

				@Override
				public Result<PayResp> toPay(ToPayReq toPayReq) {
					return Result.onFallbackError();
				}

				@Override
				public Result<Void> refund(PayRefundReq payRefundReq) {
					return Result.onFallbackError();
				}

			};
		}
	}
}
