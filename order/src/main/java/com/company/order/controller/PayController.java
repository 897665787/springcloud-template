package com.company.order.controller;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.company.common.api.Result;
import com.company.framework.amqp.MessageSender;
import com.company.framework.redis.redisson.DistributeLockUtils;
import com.company.order.amqp.rabbitmq.Constants;
import com.company.order.amqp.strategy.StrategyConstants;
import com.company.order.api.enums.OrderPayEnum;
import com.company.order.api.enums.OrderPayRefundEnum;
import com.company.order.api.feign.PayFeign;
import com.company.order.api.request.PayCloseReq;
import com.company.order.api.request.PayRefundReq;
import com.company.order.api.request.PayReq;
import com.company.order.api.request.RefundReq;
import com.company.order.api.response.PayInfoResp;
import com.company.order.api.response.PayRefundResp;
import com.company.order.api.response.PayResp;
import com.company.order.api.response.PayTradeStateResp;
import com.company.order.entity.OrderPay;
import com.company.order.entity.OrderPayRefund;
import com.company.order.innercallback.processor.bean.InnerCallbackProcessorBeanName;
import com.company.order.innercallback.processor.bean.ProcessorBeanName;
import com.company.order.innercallback.service.IInnerCallbackService;
import com.company.order.pay.PayFactory;
import com.company.order.pay.core.PayClient;
import com.company.order.pay.dto.PayParams;
import com.company.order.service.OrderPayRefundService;
import com.company.order.service.OrderPayService;
import com.google.common.collect.Maps;

import cn.hutool.core.date.LocalDateTimeUtil;

@RestController
@RequestMapping(value = "/pay")
public class PayController implements PayFeign {

	@Autowired
	private OrderPayService orderPayService;

	@Autowired
	private MessageSender messageSender;
	
	@Autowired
	private OrderPayRefundService orderPayRefundService;

	@Autowired
	private IInnerCallbackService innerCallbackService;
	
	private static final String NOTIFY_URL_REFUND = "http://template-order/pay/refundWithRetry";
	
	@Override
	public Result<PayResp> unifiedorder(@RequestBody @Valid PayReq payReq) {

		String orderCode = payReq.getOrderCode();
		String key = String.format("lock:orderpay:ordercode:%s", orderCode);
		PayResp payRespR = DistributeLockUtils
				.doInDistributeLockThrow(key, () -> {
					OrderPay orderPay = orderPayService.selectByOrderCode(orderCode);
					if (orderPay == null) {
						orderPay = new OrderPay();
						orderPay.setUserId(payReq.getUserId());
						orderPay.setOrderCode(orderCode);
						orderPay.setBusinessType(payReq.getBusinessType().getCode());
						orderPay.setMethod(payReq.getMethod().getCode());
						orderPay.setAppid(payReq.getAppid());
						orderPay.setAmount(payReq.getAmount());
						orderPay.setBody(payReq.getBody());
						orderPay.setStatus(OrderPayEnum.Status.WAITPAY.getCode());
						orderPay.setNotifyUrl(payReq.getNotifyUrl());
						orderPay.setNotifyAttach(payReq.getAttach());
						orderPay.setRemark(payReq.getRemark());
						
						orderPayService.insert(orderPay);
					} else {
						OrderPay orderPay4Update = new OrderPay();
						orderPay4Update.setId(orderPay.getId());
						orderPay4Update.setBusinessType(payReq.getBusinessType().getCode());
						orderPay4Update.setMethod(payReq.getMethod().getCode());
						orderPay4Update.setAppid(payReq.getAppid());
						orderPay4Update.setAmount(payReq.getAmount());
						orderPay.setBody(payReq.getBody());
						orderPay4Update.setStatus(OrderPayEnum.Status.WAITPAY.getCode());
						orderPay4Update.setNotifyUrl(payReq.getNotifyUrl());
						orderPay4Update.setNotifyAttach(payReq.getAttach());
						orderPay4Update.setRemark(payReq.getRemark());
						
						orderPayService.updateById(orderPay4Update);
					}

					PayParams payParams = new PayParams();

					// 业务参数
					payParams.setUserId(payReq.getUserId());

					// 支付参数
					payParams.setAppid(payReq.getAppid());
					payParams.setAmount(payReq.getAmount().setScale(2, BigDecimal.ROUND_HALF_UP));// 向上取整，保留2位小数
					payParams.setBody(payReq.getBody());
					payParams.setOutTradeNo(orderCode);
					payParams.setSpbillCreateIp(payReq.getSpbillCreateIp());
					payParams.setProductId(payReq.getProductId());
					payParams.setOpenid(payReq.getOpenid());
					
					PayClient tradeClient = PayFactory.of(payReq.getMethod());
					PayResp payResp = tradeClient.pay(payParams);
					payTimeout(orderCode, payReq.getTimeoutSeconds());// 订单超时处理
					
					return payResp;
				});
		return Result.success(payRespR);
	}

	/**
	 * 订单超时未支付延时任务
	 * 
	 * @param orderCode
	 *            订单号
	 * @param timeoutSeconds
	 *            超时秒数
	 */
	private void payTimeout(String orderCode, Integer timeoutSeconds) {
		Map<String, Object> params = Maps.newHashMap();
		params.put("orderCode", orderCode);
		if (timeoutSeconds == null) {
			timeoutSeconds = 1800;// 默认30分钟,1800秒
		}
		params.put("delaySeconds", timeoutSeconds);

		messageSender.sendDelayMessage(StrategyConstants.PAY_TIMEOUT_STRATEGY, params, Constants.EXCHANGE.XDELAYED,
				Constants.QUEUE.XDELAYED.ROUTING_KEY, timeoutSeconds);
	}
	
	@Override
	public Result<PayInfoResp> queryPayInfo(String orderCode) {
		OrderPay orderPay = orderPayService.selectByOrderCode(orderCode);
		if (orderPay == null) {
			return Result.fail("数据不存在");
		}
		PayClient tradeClient = PayFactory.of(OrderPayEnum.Method.of(orderPay.getMethod()));
		Object payInfo = tradeClient.getPayInfo(orderCode);

		PayInfoResp payInfoResp = new PayInfoResp();
		payInfoResp.setPayInfo(payInfo);

		return Result.success(payInfoResp);
	}

	@Override
	public Result<PayTradeStateResp> queryTradeState(String orderCode) {
		OrderPay orderPay = orderPayService.selectByOrderCode(orderCode);
		if (orderPay == null) {
			return Result.fail("数据不存在");
		}
		PayClient tradeClient = PayFactory.of(OrderPayEnum.Method.of(orderPay.getMethod()));
		PayTradeStateResp payTradeState = tradeClient.queryTradeState(orderCode);
		return Result.success(payTradeState);
	}

	@Override
	public Result<Void> refund(@RequestBody @Valid PayRefundReq payRefundReq) {
		String outTradeNo = payRefundReq.getOrderCode();
		OrderPay orderPay = orderPayService.selectByOrderCode(outTradeNo);
		if (orderPay == null) {
			return Result.fail("订单不存在");
		}
		
		if (OrderPayEnum.Status.of(orderPay.getStatus()) != OrderPayEnum.Status.PAYED) {
			// 原订单不是已支付状态，不允许退款
			return Result.fail("订单不是完成状态，不允许退款");
		}

		String outRefundNo = payRefundReq.getRefundOrderCode();
		String key = String.format("lock:orderrefund:ordercode:%s", outRefundNo);
		String message = DistributeLockUtils
				.doInDistributeLockThrow(key, () -> {
					OrderPayRefund refundOrderPay = orderPayRefundService.selectByRefundOrderCode(outRefundNo);
					if (refundOrderPay != null) {
						return null;
					}
					// 已退款金额是否已达到订单总金额
					BigDecimal sumRefundAmount = orderPayRefundService.sumRefundAmount(outTradeNo);
					if (orderPay.getAmount().compareTo(sumRefundAmount) <= 0) {
						return "已全额退款";
					}

					BigDecimal leftAmount = orderPay.getAmount().subtract(sumRefundAmount);// 剩余金额
					// 退款金额与剩余金额比较
					BigDecimal refundAmount = payRefundReq.getRefundAmount();
					if (refundAmount != null) {
						if (refundAmount.compareTo(leftAmount) > 0) {
							return "退款金额超出可退款金额";
						}
					} else {
						refundAmount = leftAmount;
					}

					// 创建退款订单
					refundOrderPay = new OrderPayRefund();
					refundOrderPay.setUserId(orderPay.getUserId());
					refundOrderPay.setBusinessType(payRefundReq.getBusinessType().getCode());
					refundOrderPay.setMethod(orderPay.getMethod());
					refundOrderPay.setOrderCode(outTradeNo);
					refundOrderPay.setRefundOrderCode(outRefundNo);
					refundOrderPay.setAmount(orderPay.getAmount());
					refundOrderPay.setRefundAmount(refundAmount);
					refundOrderPay.setStatus(OrderPayRefundEnum.Status.WAIT_APPLY.getCode());
					refundOrderPay.setRemark(payRefundReq.getRefundRemark());
					refundOrderPay.setNotifyUrl(payRefundReq.getNotifyUrl());
					refundOrderPay.setNotifyAttach(payRefundReq.getAttach());
					orderPayRefundService.insert(refundOrderPay);
					return null;
				});

		if (message != null) {
			return Result.fail(message);
		}

		// 异步处理 ========> 逻辑上等同于直接调用 refundWithRetry
		RefundReq refundReq = new RefundReq();
		refundReq.setOutRefundNo(outRefundNo);

		ProcessorBeanName processorBeanName = new ProcessorBeanName();
		processorBeanName.setAbandonRequest(InnerCallbackProcessorBeanName.REFUND_FAIL_PROCESSOR);

		innerCallbackService.postRestTemplate(NOTIFY_URL_REFUND, refundReq, processorBeanName);
		
		return Result.success();
	}
	
	@Override
	public Result<Void> refundWithRetry(@RequestBody RefundReq refundReq) {
		OrderPayRefund refundOrderPay = orderPayRefundService.selectByRefundOrderCode(refundReq.getOutRefundNo());

		if (refundOrderPay == null) {
			return Result.fail("数据不存在");
		}

		String outTradeNo = refundOrderPay.getOrderCode();
		BigDecimal refundAmount = refundOrderPay.getRefundAmount().setScale(2, BigDecimal.ROUND_HALF_UP);// 向上取整，保留2位小数

		PayClient payClient = PayFactory.of(OrderPayEnum.Method.of(refundOrderPay.getMethod()));
		PayRefundResp payRefundResp = payClient.refund(outTradeNo, refundOrderPay.getOrderCode(), refundAmount);

		if (!payRefundResp.getSuccess()) {
			return Result.fail(payRefundResp.getMessage());
		}

		return Result.success();
	}

	@Override
	public Result<Void> payClose(@RequestBody @Valid PayCloseReq payCloseReq) {
		OrderPay orderPay = orderPayService.selectByOrderCode(payCloseReq.getOrderCode());

		if (orderPay == null) {
			return Result.fail("数据不存在");
		}
		OrderPayEnum.Status status = OrderPayEnum.Status.of(orderPay.getStatus());
		if (status != OrderPayEnum.Status.WAITPAY) {
			// 订单不是未支付状态，不允许关闭
			return Result.fail("订单不是待支付状态，不允许关闭");
		}

		Map<String, Object> params = new HashMap<>();
		params.put("outTradeNo", payCloseReq.getOrderCode());
		
		LocalDateTime minPayCloseTime = orderPay.getCreateTime().plusMinutes(5);
		LocalDateTime now = LocalDateTime.now();
		// 微信订单不能创建后马上关闭，延迟调用微信关闭接口时间 单位：s
		// 官方文档：https://pay.weixin.qq.com/wiki/doc/api/H5.php?chapter=9_3&index=3
		Integer delay = 0;
		if (minPayCloseTime.compareTo(now) > 0) {
			delay = (int) LocalDateTimeUtil.between(now, minPayCloseTime, ChronoUnit.SECONDS);
		}
		
		messageSender.sendDelayMessage(StrategyConstants.PAY_CLOSE_STRATEGY, params, Constants.EXCHANGE.XDELAYED,
				Constants.QUEUE.XDELAYED.ROUTING_KEY, delay);
		
		return Result.success();
	}
}
