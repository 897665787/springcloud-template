package com.company.app.controller;

import java.time.LocalDateTime;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.company.common.api.Result;
import com.company.framework.annotation.RequireLogin;
import com.company.framework.context.HttpContextUtil;
import com.company.order.api.enums.OrderEnum;
import com.company.order.api.feign.OrderFeign;
import com.company.order.api.feign.PayFeign;
import com.company.order.api.request.OrderCancelReq;
import com.company.order.api.response.OrderDetailResp;
import com.company.order.api.response.OrderResp;
import com.company.order.api.response.PayInfoResp;

/**
 * 用户订单中心
 */
@RequireLogin
@RestController
@RequestMapping("/orderCenter")
public class OrderCenterController {

	@Autowired
	private OrderFeign orderFeign;
	@Autowired
	private PayFeign payFeign;

	/**
	 * 分页查询订单列表
	 * 
	 * @param current
	 * @param size
	 * @param status
	 * @return
	 */
	@GetMapping("/page")
	public Result<List<OrderResp>> page(
			@Valid @NotNull(message = "缺少参数当前页") @Min(value = 1, message = "当前页不能小于1") Integer current,
			@Valid @NotNull(message = "缺少参数每页数量") Integer size, OrderEnum.StatusEnum status) {
		return orderFeign.page(current, size, status);
	}

	/**
	 * 订单详情
	 * 
	 * @param orderCode
	 * @return
	 */
	@GetMapping("/detail")
	public Result<OrderDetailResp> detail(@Valid @NotNull(message = "订单号不能为空") String orderCode) {
		return orderFeign.queryByOrderCode(orderCode);
	}

	/**
	 * 取消订单
	 * 
	 * @param orderCode
	 * @return
	 */
	@GetMapping("/cancel")
	public Result<OrderResp> cancel(@Valid @NotNull(message = "订单号不能为空") String orderCode) {
		Integer userId = HttpContextUtil.currentUserIdInt();
		orderFeign.validOrderCodeUserId(orderCode, userId).dataOrThrow();
		
		OrderCancelReq orderCancelReq = new OrderCancelReq();
		orderCancelReq.setOrderCode(orderCode);
		orderCancelReq.setCancelTime(LocalDateTime.now());
		return orderFeign.cancel(orderCancelReq);
	}

	/**
	 * 去支付
	 * 
	 * @param orderCode
	 * @return
	 */
	@GetMapping("/toPay")
	public Result<PayInfoResp> toPay(@Valid @NotNull(message = "订单号不能为空") String orderCode) {
		return payFeign.queryPayInfo(orderCode);
	}
}
