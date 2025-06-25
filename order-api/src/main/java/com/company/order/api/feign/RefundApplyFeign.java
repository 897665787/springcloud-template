package com.company.order.api.feign;

import java.util.List;

import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.company.common.api.Result;
import com.company.order.api.constant.Constants;
import com.company.order.api.request.PayRefundApplyReq;

@FeignClient(value = Constants.FEIGNCLIENT_VALUE, path = "/refundApply", fallbackFactory = RefundApplyFeign.RefundApplyFeignFactory.class)
public interface RefundApplyFeign {

	/**
	 * 退款申请
	 *
	 * @param payRefundApplyReq
	 * @return
	 */
	@PostMapping("/refundApply")
	Integer refundApply(@RequestBody PayRefundApplyReq payRefundApplyReq);

	@GetMapping("/selectId4Deal")
	List<Integer> selectId4Deal();

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
	Boolean dealRefundApply(@RequestParam("id") Integer id);

	@Component
	class RefundApplyFeignFactory implements FallbackFactory<RefundApplyFeign> {

		@Override
		public RefundApplyFeign create(Throwable throwable) {
			return new RefundApplyFeign() {

				@Override
				public Integer refundApply(PayRefundApplyReq payRefundApplyReq) {
					return Result.onFallbackError();
				}

				@Override
				public List<Integer> selectId4Deal() {
					return Result.onFallbackError();
				}

				@Override
				public Boolean dealRefundApply(Integer id) {
					return Result.onFallbackError();
				}
			};
		}
	}
}
