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
import com.company.order.api.request.OrderReceiveReq;
import com.company.order.api.request.OrderRefundApplyReq;
import com.company.order.api.request.OrderRefundFailReq;
import com.company.order.api.request.OrderRefundFinishReq;
import com.company.order.api.request.RegisterOrderReq;
import com.company.order.api.response.Order4Resp;
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
	Result<Void> registerOrder(@RequestBody RegisterOrderReq registerOrderReq);

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
	Result<Boolean> cancelByTimeout(@RequestBody OrderCancelReq orderCancelReq);
	
	/**
	 * 修改订单状态（支付成功）
	 * 
	 * @param orderPaySuccessReq
	 * @return
	 */
	@PostMapping("/paySuccess")
	Result<Boolean> paySuccess(@RequestBody OrderPaySuccessReq orderPaySuccessReq);
	
	/**
	 * 修改订单状态（确认收货）
	 *
	 * @param orderReceiveReq
	 * @return
	 */
	@PostMapping("/receive")
	Result<Boolean> receive(@RequestBody OrderReceiveReq orderReceiveReq);
	
	/**
	 * 修改订单状态（完成订单）
	 * 
	 * @param orderFinishReq
	 * @return
	 */
	@PostMapping("/finish")
	Result<Boolean> finish(@RequestBody OrderFinishReq orderFinishReq);

	@PostMapping("/refundApply")
	Result<OrderRefundApplyResp> refundApply(@RequestBody OrderRefundApplyReq orderRefundApplyReq);

	@PostMapping("/refundFail")
	Result<Boolean> refundFail(@RequestBody OrderRefundFailReq orderRefundFailReq);

	@PostMapping("/refundFinish")
	Result<Boolean> refundFinish(@RequestBody OrderRefundFinishReq orderRefundFinishReq);

	@GetMapping("/deleteOrder")
	Result<Void> deleteOrder(@RequestParam("orderCode") String orderCode);
	
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
	
	/**
	 * 查询超时未收货订单号改为已收货(job)
	 * 
	 * @param limit
	 * @return
	 */
	@GetMapping("/select4OverSendSuccess")
	Result<List<String>> select4OverSendSuccess(@RequestParam("limit") Integer limit);

	/**
	 * 查询超时未评价订单号改为已完成(job)
	 * 
	 * @param limit
	 * @return
	 */
	@GetMapping("/select4OverWaitReview")
	Result<List<String>> select4OverWaitReview(@RequestParam("limit") Integer limit);

	@GetMapping("/selectByOrderCode")
	Result<Order4Resp> selectByOrderCode(@RequestParam("orderCode") String orderCode);
}
