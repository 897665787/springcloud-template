package com.company.order.api.feign;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.company.common.api.Result;
import com.company.order.api.constant.Constants;
import com.company.order.api.enums.OrderEnum;
import com.company.order.api.feign.fallback.OrderFeignFallback;
import com.company.order.api.request.OrderCancelReq;
import com.company.order.api.request.OrderFinishReq;
import com.company.order.api.request.OrderPaySuccessReq;
import com.company.order.api.request.OrderRefundApplyReq;
import com.company.order.api.request.OrderRefundFinishReq;
import com.company.order.api.request.OrderRefundRejectReq;
import com.company.order.api.request.RegisterOrderReq;
import com.company.order.api.response.OrderDetailResp;
import com.company.order.api.response.OrderRefundApplyResp;
import com.company.order.api.response.OrderResp;

@FeignClient(value = Constants.FEIGNCLIENT_VALUE, path = "/order", fallbackFactory = OrderFeignFallback.class)
public interface OrderFeign {

	/**
	 * 注册订单
	 * 
	 * @param registerOrderReq
	 * @return
	 */
	@PostMapping("/registerOrder")
	Result<OrderResp> registerOrder(@RequestBody RegisterOrderReq registerOrderReq);

	/**
	 * 修改订单状态（用户取消订单）
	 * 
	 * @param orderCancelReq
	 * @return
	 */
	@PostMapping("/cancelByUser")
	Result<OrderDetailResp> cancelByUser(@RequestBody OrderCancelReq orderCancelReq);
	
	/**
	 * 修改订单状态（超时取消订单）
	 * 
	 * @param orderCancelReq
	 * @return
	 */
	@PostMapping("/cancelByTimeout")
	Result<Void> cancelByTimeout(@RequestBody OrderCancelReq orderCancelReq);
	
	/**
	 * 修改订单状态（支付成功）
	 * 
	 * @param orderPaySuccessReq
	 * @return
	 */
	@PostMapping("/paySuccess")
	Result<Void> paySuccess(@RequestBody OrderPaySuccessReq orderPaySuccessReq);
	
	/**
	 * 修改订单状态（完成订单）
	 * 
	 * @param orderFinishReq
	 * @return
	 */
	@PostMapping("/finish")
	Result<Void> finish(@RequestBody OrderFinishReq orderFinishReq);

	@PostMapping("/refundApply")
	Result<OrderRefundApplyResp> refundApply(@RequestBody OrderRefundApplyReq orderRefundApplyReq);

	@PostMapping("/refundReject")
	Result<Void> refundReject(@RequestBody OrderRefundRejectReq orderRefundRejectReq);

	@PostMapping("/refundFinish")
	Result<Void> refundFinish(@RequestBody OrderRefundFinishReq orderRefundFinishReq);
	
	/**
	 * 分页查询订单列表
	 * 
	 * @param id
	 * @return
	 */
	@GetMapping("/page")
	Result<List<OrderResp>> page(@RequestParam("current") Integer current, @RequestParam("size") Integer size,
			@RequestParam(value = "status", required = false) OrderEnum.StatusEnum status);

	/**
	 * 根据订单号查询订单详情
	 * 
	 * @param orderCode
	 * @return
	 */
	@GetMapping("/detail")
	Result<OrderDetailResp> detail(@RequestParam("orderCode") String orderCode);
	
}
