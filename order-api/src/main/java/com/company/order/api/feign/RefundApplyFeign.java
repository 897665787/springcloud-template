package com.company.order.api.feign;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.company.common.api.Result;
import com.company.order.api.constant.Constants;
import com.company.order.api.request.PayRefundApplyReq;
import com.company.order.api.request.RefundNotifyReq;

import feign.hystrix.FallbackFactory;

@FeignClient(value = Constants.FEIGNCLIENT_VALUE, path = "/refundApply", fallbackFactory = RefundApplyFeign.RefundApplyFeignFactory.class)
public interface RefundApplyFeign {

	/**
	 * 退款申请
	 * 
	 * @param payRefundApplyReq
	 * @return
	 */
	@PostMapping("/refundApply")
	Result<Integer> refundApply(@RequestBody PayRefundApplyReq payRefundApplyReq);

	@GetMapping("/selectId4Deal")
	Result<List<Integer>> selectId4Deal();

	/**
	 * 处理退款申请
	 * 
	 * <pre>
	 * 申请结果或退款结果会以广播方式通知到各业务
	 * </pre>
	 * 
	 * @param id
	 * @return
	 */
	@PostMapping("/dealRefundApply")
	Result<Boolean> dealRefundApply(@RequestParam("id") Integer id);

	/**
	 * 退款回调
	 * 
	 * @param refundNotifyReq
	 * @return
	 */
	@PostMapping("/refundNotify")
	Result<Void> refundNotify(@RequestBody RefundNotifyReq refundNotifyReq);

	@Component
	class RefundApplyFeignFactory implements FallbackFactory<RefundApplyFeign> {

		@Override
		public RefundApplyFeign create(Throwable throwable) {
			return new RefundApplyFeign() {

				@Override
				public Result<Integer> refundApply(PayRefundApplyReq payRefundApplyReq) {
					return Result.onFallbackError();
				}

				@Override
				public Result<List<Integer>> selectId4Deal() {
					return Result.onFallbackError();
				}

				@Override
				public Result<Boolean> dealRefundApply(Integer id) {
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
