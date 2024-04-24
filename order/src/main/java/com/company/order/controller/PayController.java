package com.company.order.controller;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;

import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.company.common.api.Result;
import com.company.common.exception.BusinessException;
import com.company.common.util.JsonUtil;
import com.company.framework.amqp.MessageSender;
import com.company.framework.lock.LockClient;
import com.company.order.amqp.rabbitmq.Constants;
import com.company.order.amqp.strategy.StrategyConstants;
import com.company.order.api.enums.OrderPayEnum;
import com.company.order.api.enums.OrderPayRefundEnum;
import com.company.order.api.feign.PayFeign;
import com.company.order.api.request.PayCloseReq;
import com.company.order.api.request.PayNotifyReq;
import com.company.order.api.request.PayRefundReq;
import com.company.order.api.request.PayReq;
import com.company.order.api.request.PayTimeoutReq;
import com.company.order.api.request.RefundReq;
import com.company.order.api.request.ToPayReq;
import com.company.order.api.response.PayCloseResp;
import com.company.order.api.response.PayRefundResp;
import com.company.order.api.response.PayResp;
import com.company.order.api.response.PayTradeStateResp;
import com.company.order.entity.OrderPay;
import com.company.order.entity.OrderPayRefund;
import com.company.order.enums.InnerCallbackEnum;
import com.company.order.innercallback.processor.bean.InnerCallbackProcessorBeanName;
import com.company.order.innercallback.processor.bean.ProcessorBeanName;
import com.company.order.innercallback.service.IInnerCallbackService;
import com.company.order.pay.PayFactory;
import com.company.order.pay.core.PayClient;
import com.company.order.pay.dto.PayParams;
import com.company.order.service.OrderPayRefundService;
import com.company.order.service.OrderPayService;

import cn.hutool.core.date.LocalDateTimeUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 收银台
 */
@Slf4j
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
	
	@Autowired
	private LockClient lockClient;
	
	private static final String NOTIFY_URL_REFUND = com.company.order.api.constant.Constants.feignUrl("/pay/refundWithRetry");
	private static final String NOTIFY_URL_TIMEOUT = com.company.order.api.constant.Constants.feignUrl("/pay/timeoutWithRetry");;
	
	@Override
	public Result<PayResp> unifiedorder(@RequestBody @Valid PayReq payReq) {

		String orderCode = payReq.getOrderCode();
		String key = String.format("lock:orderpay:ordercode:%s", orderCode);
		PayResp payRespR = lockClient.doInLock(key, () -> {
			OrderPay orderPay = orderPayService.selectByOrderCode(orderCode);
			if (orderPay == null) {
				orderPay = new OrderPay();
				orderPay.setUserId(payReq.getUserId());
				orderPay.setOrderCode(orderCode);
				orderPay.setBusinessType(payReq.getBusinessType().getCode());
				orderPay.setMethod(payReq.getMethod().getCode());
				orderPay.setAmount(payReq.getAmount());
				orderPay.setBody(payReq.getBody());
				orderPay.setProductId(payReq.getProductId());
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
				orderPay4Update.setAmount(payReq.getAmount());
				orderPay4Update.setBody(payReq.getBody());
				orderPay4Update.setProductId(payReq.getProductId());
				orderPay4Update.setStatus(OrderPayEnum.Status.WAITPAY.getCode());
				orderPay4Update.setNotifyUrl(payReq.getNotifyUrl());
				orderPay4Update.setNotifyAttach(payReq.getAttach());
				orderPay4Update.setRemark(payReq.getRemark());

				orderPayService.updateById(orderPay4Update);
			}

			payTimeout(orderCode, payReq.getTimeoutSeconds());// 订单超时处理

			// 支付参数
			PayParams payParams = new PayParams();
			payParams.setAppid(payReq.getAppid());
			payParams.setMchid(payReq.getMchid());
			payParams.setAmount(payReq.getAmount().setScale(2, BigDecimal.ROUND_HALF_UP));// 向上取整，保留2位小数
			payParams.setBody(payReq.getBody());
			payParams.setOutTradeNo(orderCode);
			payParams.setSpbillCreateIp(payReq.getSpbillCreateIp());
			payParams.setProductId(payReq.getProductId());
			payParams.setOpenid(payReq.getOpenid());

			PayClient tradeClient = PayFactory.of(payReq.getMethod());
			PayResp payResp = tradeClient.pay(payParams);
			if (!payResp.getSuccess()) {
				throw new BusinessException(payResp.getMessage());
			}

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
		// 异步处理 ========> 逻辑上等同于直接调用 timeoutWithRetry
		PayTimeoutReq payTimeoutReq = new PayTimeoutReq();
		payTimeoutReq.setOrderCode(orderCode);

		ProcessorBeanName processorBeanName = new ProcessorBeanName();
		processorBeanName.setAbandonRequest(InnerCallbackProcessorBeanName.PAYTIMEOUT_FAIL_PROCESSOR);

		if (timeoutSeconds == null) {
			timeoutSeconds = 1800;// 默认30分钟,1800秒
		}
		LocalDateTime timeoutTime = LocalDateTime.now().plusSeconds(timeoutSeconds);
		innerCallbackService.postRestTemplate(NOTIFY_URL_TIMEOUT, payTimeoutReq, processorBeanName, 2, 10,
				InnerCallbackEnum.SecondsStrategy.INCREMENT, timeoutTime);
	}

	/**
	 * 订单超时处理(使用restTemplate的方式调用)
	 * 
	 * @param payTimeoutReq
	 * @return
	 */
	@PostMapping("/timeoutWithRetry")
	public Result<Boolean> timeoutWithRetry(@RequestBody @Valid PayTimeoutReq payTimeoutReq) {
		String orderCode = payTimeoutReq.getOrderCode();
		
		OrderPay orderPay = orderPayService.selectByOrderCode(orderCode);
		if (orderPay == null) {
			return Result.fail("数据不存在");
		}
		OrderPayEnum.Status status = OrderPayEnum.Status.of(orderPay.getStatus());
		if (status != OrderPayEnum.Status.WAITPAY) {
			// 订单不是未支付状态，不允许关闭
			return Result.success();
		}

		PayClient payClient = PayFactory.of(OrderPayEnum.Method.of(orderPay.getMethod()));
		PayTradeStateResp payTradeState = payClient.queryTradeState(orderCode);
		if (!payTradeState.getResult()) {
			return Result.fail(payTradeState.getMessage());
		}
		if (payTradeState.getPaySuccess()) {
			// 订单已支付
			return Result.success();
		}
		
		PayCloseResp payCloseResp = payClient.payClose(orderCode);
		log.info("关闭订单结果:{}", JsonUtil.toJsonString(payCloseResp));
		if (!payCloseResp.getSuccess()) {
			return Result.fail(payCloseResp.getMessage());
		}
		
		// 修改状态为已关闭
		OrderPay orderPay4Update = new OrderPay();
		orderPay4Update.setStatus(OrderPayEnum.Status.CLOSED.getCode());
		LocalDateTime time = LocalDateTime.now();
		orderPay4Update.setPayTime(time);
		EntityWrapper<OrderPay> wrapper = new EntityWrapper<>();
		wrapper.eq("id", orderPay.getId());
		wrapper.eq("status", OrderPayEnum.Status.WAITPAY.getCode());
		boolean affect = orderPayService.update(orderPay4Update, wrapper);
		if (!affect) {// 更新不成功，说明订单不是处理中状态
			log.info("update订单不是处理中状态，不操作关闭:{}", orderCode);
			return Result.success();
		}

		String notifyUrl = orderPay.getNotifyUrl();
		if (StringUtils.isBlank(notifyUrl)) {
			log.info("无回调URL");
			return Result.success();
		}

		// 回调超时未支付关闭订单到对应业务中
		PayNotifyReq payNotifyReq = new PayNotifyReq();
		payNotifyReq.setEvent(PayNotifyReq.EVENT.CLOSE);
		payNotifyReq.setOrderCode(orderCode);
		payNotifyReq.setTime(time);
		payNotifyReq.setAttach(orderPay.getNotifyAttach());

		log.info("超时未支付关闭订单回调,请求地址:{},参数:{}", notifyUrl, JsonUtil.toJsonString(payNotifyReq));
		innerCallbackService.postRestTemplate(notifyUrl, payNotifyReq);

		return Result.success();
	}

	@Override
	public Result<Void> payClose(@RequestBody @Valid PayCloseReq payCloseReq) {
		String orderCode = payCloseReq.getOrderCode();
		OrderPay orderPay = orderPayService.selectByOrderCode(orderCode);
		if (orderPay == null) {
			return Result.fail("数据不存在");
		}
		OrderPayEnum.Status status = OrderPayEnum.Status.of(orderPay.getStatus());
		if (status != OrderPayEnum.Status.WAITPAY) {
			// 订单不是未支付状态，不允许关闭
			return Result.fail("订单不是待支付状态，不允许关闭");
		}
		
		// 修改状态为已关闭
		OrderPay orderPay4Update = new OrderPay();
		orderPay4Update.setStatus(OrderPayEnum.Status.CLOSED.getCode());
		EntityWrapper<OrderPay> wrapper = new EntityWrapper<>();
		wrapper.eq("id", orderPay.getId());
		wrapper.eq("status", OrderPayEnum.Status.WAITPAY.getCode());
		boolean affect = orderPayService.update(orderPay4Update, wrapper);
		if (!affect) {// 更新不成功，说明订单不是处理中状态
			log.info("update订单不是处理中状态，不操作关闭:{}", orderCode);
			return Result.success();
		}
		
		Map<String, Object> params = new HashMap<>();
		params.put("outTradeNo", orderCode);
		
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
	
	@Override
	public Result<PayResp> toPay(@RequestBody ToPayReq toPayReq) {
		String orderCode = toPayReq.getOrderCode();
		OrderPay orderPay = orderPayService.selectByOrderCode(orderCode);
		if (orderPay == null) {
			return Result.fail("数据不存在");
		}

		if (OrderPayEnum.Status.of(orderPay.getStatus()) != OrderPayEnum.Status.WAITPAY) {
			return Result.fail("订单不是待支付状态，不允许支付");
		}
		
		OrderPayEnum.Method method = toPayReq.getMethod();
		if (method == null) {
			method = OrderPayEnum.Method.of(orderPay.getMethod());
		} else {// 更换支付方式
			OrderPay orderPay4Update = new OrderPay();
			orderPay4Update.setId(orderPay.getId());
			orderPay4Update.setMethod(method.getCode());
			orderPayService.updateById(orderPay4Update);
		}
		
		// 支付参数
		PayParams payParams = new PayParams();
		payParams.setAppid(toPayReq.getAppid());
		payParams.setAmount(orderPay.getAmount().setScale(2, BigDecimal.ROUND_HALF_UP));// 向上取整，保留2位小数
		payParams.setBody(orderPay.getBody());
		payParams.setOutTradeNo(orderCode);
		payParams.setSpbillCreateIp(toPayReq.getSpbillCreateIp());
		payParams.setProductId(orderPay.getProductId());
		payParams.setOpenid(toPayReq.getOpenid());
		
		PayClient tradeClient = PayFactory.of(method);
		PayResp payResp = tradeClient.pay(payParams);
		if (!payResp.getSuccess()) {
			throw new BusinessException(payResp.getMessage());
		}

		return Result.success(payResp);
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
		String message = lockClient.doInLock(key, () -> {
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
	
	/**
	 * 退款处理(使用restTemplate的方式调用)
	 * 
	 * @param refundReq
	 * @return
	 */
	@PostMapping("/refundWithRetry")
	public Result<Void> refundWithRetry(@RequestBody RefundReq refundReq) {
		OrderPayRefund orderPayRefund = orderPayRefundService.selectByRefundOrderCode(refundReq.getOutRefundNo());
		if (orderPayRefund == null) {
			return Result.fail("数据不存在");
		}

		String outTradeNo = orderPayRefund.getOrderCode();
		BigDecimal refundAmount = orderPayRefund.getRefundAmount().setScale(2, BigDecimal.ROUND_HALF_UP);// 向上取整，保留2位小数

		PayClient payClient = PayFactory.of(OrderPayEnum.Method.of(orderPayRefund.getMethod()));
		PayRefundResp payRefundResp = payClient.refund(outTradeNo, orderPayRefund.getOrderCode(), refundAmount);

		if (!payRefundResp.getSuccess()) {
			return Result.fail(payRefundResp.getMessage());
		}

		return Result.success();
	}

}
