package com.company.order.amqp.strategy;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.company.framework.amqp.BaseStrategy;
import com.company.order.api.enums.OrderPayEnum;
import com.company.order.api.enums.OrderPayRefundEnum;
import com.company.order.api.request.RefundNotifyReq;
import com.company.order.entity.OrderPayRefund;
import com.company.order.innercallback.service.IInnerCallbackService;
import com.company.order.mapper.PayNotifyMapper;
import com.company.order.service.FinancialFlowService;
import com.company.order.service.OrderPayRefundService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component(StrategyConstants.REFUND_NOTIFY_STRATEGY)
public class RefundNotifyStrategy implements BaseStrategy<Map<String, Object>> {

	@Autowired
	private OrderPayRefundService orderPayRefundService;
	@Autowired
	private PayNotifyMapper payNotifyMapper;
	@Autowired
	private IInnerCallbackService innerCallbackService;

	@Resource
	private FinancialFlowService financialFlowService;

	@Override
	public void doStrategy(Map<String, Object> params) {
		Integer payNotifyId = MapUtils.getInteger(params, "payNotifyId");

		String outTradeNo = MapUtils.getString(params, "outTradeNo");
		String outRefundNo = MapUtils.getString(params, "outRefundNo");
		Boolean success = MapUtils.getBoolean(params, "success");
		OrderPayRefund orderPayRefund = orderPayRefundService.selectByRefundOrderCode(outRefundNo);

		if (success) {
			// 退款成功
			OrderPayRefund orderPayRefund4Update = new OrderPayRefund();
			orderPayRefund4Update.setId(orderPayRefund.getId());
			orderPayRefund4Update.setStatus(OrderPayRefundEnum.Status.REFUND_SUCCESS.getCode());
			orderPayRefundService.updateById(orderPayRefund4Update);

			financialFlow(params, outTradeNo, outRefundNo);


		} else {
			// 退款失败
			OrderPayRefund orderPayRefund4Update = new OrderPayRefund();
			orderPayRefund4Update.setId(orderPayRefund.getId());
			orderPayRefund4Update.setStatus(OrderPayRefundEnum.Status.REFUND_FAIL.getCode());
			orderPayRefundService.updateById(orderPayRefund4Update);
		}

		// 回调退款到对应业务中
		String notifyUrl = orderPayRefund.getNotifyUrl();
		if (StringUtils.isBlank(notifyUrl)) {
			payNotifyMapper.updateRemarkById("无回调URL", payNotifyId);
			return;
		}
		
		RefundNotifyReq refundNotifyReq = new RefundNotifyReq();
		refundNotifyReq.setSuccess(success);
		String message = MapUtils.getString(params, "message");
		refundNotifyReq.setMessage(message);
		refundNotifyReq.setOrderCode(outTradeNo);
		refundNotifyReq.setRefundOrderCode(outRefundNo);
		refundNotifyReq.setAttach(orderPayRefund.getNotifyAttach());
		refundNotifyReq.setThisRefundAmount(orderPayRefund.getAmount());

		List<OrderPayRefund> refundOrderList = orderPayRefundService.listByOrderCode(outTradeNo);
		// 该订单所有的‘退款成功’订单金额求和
		BigDecimal totalRefundAmount = refundOrderList.stream()
				.filter(o -> OrderPayRefundEnum.Status.REFUND_SUCCESS.getCode().equals(o.getStatus()))
				.map(OrderPayRefund::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);

		refundNotifyReq.setTotalRefundAmount(totalRefundAmount);

		innerCallbackService.postRestTemplate(notifyUrl, refundNotifyReq);
	}

	private void financialFlow(Map<String, Object> params, String outTradeNo, String outRefundNo) {
		//数据落库, 生成财务流水(出账)
		try {
			String tradeNo = MapUtils.getString(params, "tradeNo");
			String amount = MapUtils.getString(params, "amount");
			String orderPayRefundMethod = MapUtils.getString(params, "orderPayRefundMethod");
			String merchantNo =  MapUtils.getString(params, "merchantNo");

			financialFlowService.outAccount(outTradeNo, outRefundNo, tradeNo, new BigDecimal(amount), OrderPayEnum.Method.of(orderPayRefundMethod), merchantNo);
		} catch (Exception e) {
			log.error("生成财务流水(出账), error: {}", e);
		}
	}
}
