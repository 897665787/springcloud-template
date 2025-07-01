package com.company.app.controller;

import java.time.LocalDateTime;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.company.app.req.RefundApplyReq;
import com.company.app.req.ToPayReq;
import com.company.common.api.Result;
import com.company.framework.util.Utils;
import com.company.framework.annotation.RequireLogin;
import com.company.framework.context.HttpContextUtil;
import com.company.framework.sequence.SequenceGenerator;
import com.company.order.api.enums.OrderEnum;
import com.company.order.api.enums.PayRefundApplyEnum;
import com.company.order.api.feign.OrderFeign;
import com.company.order.api.feign.PayFeign;
import com.company.order.api.feign.RefundApplyFeign;
import com.company.order.api.request.OrderCancelReq;
import com.company.order.api.request.OrderRefundApplyReq;
import com.company.order.api.request.PayRefundApplyReq;
import com.company.order.api.response.OrderDetailResp;
import com.company.order.api.response.OrderRefundApplyResp;
import com.company.order.api.response.OrderResp;
import com.company.order.api.response.PayResp;

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
	@Autowired
	private RefundApplyFeign refundApplyFeign;
	@Autowired
	private SequenceGenerator sequenceGenerator;

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
		return orderFeign.detail(orderCode);
	}

	/**
	 * 取消订单
	 *
	 * @param orderCode
	 * @return
	 */
	@GetMapping("/cancel")
	public Result<OrderDetailResp> cancel(@Valid @NotNull(message = "订单号不能为空") String orderCode) {
		OrderCancelReq orderCancelReq = new OrderCancelReq();
		orderCancelReq.setOrderCode(orderCode);
		orderCancelReq.setCancelTime(LocalDateTime.now());
		return orderFeign.cancelByUser(orderCancelReq);
	}

	/**
	 * 去支付（获取支付参数）
	 *
	 * @param toPayReq
	 * @return
	 */
	@PostMapping("/toPay")
	public Result<Object> toPay(@Valid @RequestBody ToPayReq toPayReq) {
		com.company.order.api.request.ToPayReq toPayReqApi = new com.company.order.api.request.ToPayReq();
		toPayReqApi.setOrderCode(toPayReq.getOrderCode());
		toPayReqApi.setMethod(toPayReq.getMethod());
		toPayReqApi.setAppid(toPayReq.getAppid());
		toPayReqApi.setSpbillCreateIp(HttpContextUtil.requestip());
		toPayReqApi.setOpenid(HttpContextUtil.deviceid());

		PayResp payResp = payFeign.toPay(toPayReqApi).dataOrThrow();
		if (!payResp.getSuccess()) {
			return Result.fail("支付失败，请稍后重试");
		}
		return Result.success(payResp.getPayInfo());
	}

	/**
	 * 退款申请
	 *
	 * @param orderCode
	 * @return
	 */
	@PostMapping("/refundApply")
	public Result<Void> refundApply(@Valid @RequestBody RefundApplyReq refundApplyReq) {
		String orderCode = refundApplyReq.getOrderCode();
		String refundReason = refundApplyReq.getRefundReason();

		OrderRefundApplyReq orderRefundApplyReq = new OrderRefundApplyReq();
		orderRefundApplyReq.setOrderCode(orderCode);
		orderRefundApplyReq.setRefundApplyTime(LocalDateTime.now());
		OrderRefundApplyResp orderRefundApplyResp = orderFeign.refundApply(orderRefundApplyReq).dataOrThrow();

		String refundOrderCode = String.valueOf(sequenceGenerator.nextId());
		PayRefundApplyReq payRefundApplyReq = new PayRefundApplyReq();
		payRefundApplyReq.setOrderCode(refundOrderCode);
		payRefundApplyReq.setOldOrderCode(orderCode);
		payRefundApplyReq.setAmount(orderRefundApplyResp.getCanRefundAmount());
		payRefundApplyReq.setBusinessType(PayRefundApplyEnum.BusinessType.USER);
		payRefundApplyReq.setVerifyStatus(PayRefundApplyEnum.VerifyStatus.WAIT_VERIFY);
		payRefundApplyReq.setReason(refundReason);

		String attach = Utils.append2Json(null, "oldSubStatus",
				String.valueOf(orderRefundApplyResp.getOldSubStatus().getStatus()));
		attach = Utils.append2Json(attach, "attach", orderRefundApplyResp.getAttach());
		payRefundApplyReq.setAttach(attach);

		refundApplyFeign.refundApply(payRefundApplyReq);

		return Result.success();
	}

	/**
	 * 删除订单
	 *
	 * @param orderCode
	 * @return
	 */
	@GetMapping("/deleteOrder")
	public Result<Void> deleteOrder(@Valid @NotNull(message = "订单号不能为空") String orderCode) {
		return orderFeign.deleteOrder(orderCode);
	}
}
