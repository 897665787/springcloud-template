package com.company.order.rabbitmq.consumer.strategy;

import java.math.BigDecimal;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.company.common.util.JsonUtil;
import com.company.framework.amqp.rabbit.BaseStrategy;
import com.company.order.api.enums.OrderPayEnum;
import com.company.order.api.request.PayNotifyReq;
import com.company.order.entity.OrderPay;
import com.company.order.innercallback.service.IInnerCallbackService;
import com.company.order.mapper.PayNotifyMapper;
import com.company.order.service.FinancialFlowService;
import com.company.order.service.OrderPayService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component(StrategyConstants.PAY_NOTIFY_STRATEGY)
public class PayNotifyStrategy implements BaseStrategy<Map<String, Object>> {

	@Autowired
	private OrderPayService orderPayService;
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
		Boolean success = MapUtils.getBoolean(params, "success");
		if (success) {
			// 支付成功
			OrderPay orderPay4Update = new OrderPay();
			orderPay4Update.setStatus(OrderPayEnum.Status.PAYED.getCode());
			Wrapper<OrderPay> wrapper = new EntityWrapper<OrderPay>();
			wrapper.eq("order_code", outTradeNo);
			wrapper.eq("status", OrderPayEnum.Status.WAITPAY.getCode());
			boolean affect = orderPayService.update(orderPay4Update, wrapper);
			if (!affect) {
				return;
			}

			financialFlow(params, outTradeNo);
		}

		OrderPay orderPay = orderPayService.selectByOrderCode(outTradeNo);

		String notifyUrl = orderPay.getNotifyUrl();
		if (StringUtils.isBlank(notifyUrl)) {
			payNotifyMapper.updateRemarkById("无回调URL", payNotifyId);
			return;
		}

		// 回调支付到对应业务中
		PayNotifyReq payNotifyReq = new PayNotifyReq();
		payNotifyReq.setEvent(PayNotifyReq.EVENT.PAY);
		payNotifyReq.setSuccess(success);
		String message = MapUtils.getString(params, "message");
		payNotifyReq.setMessage(message);
		payNotifyReq.setOrderCode(outTradeNo);
		payNotifyReq.setAttach(orderPay.getNotifyAttach());

		log.info("支付回调,请求地址:{},参数:{}", notifyUrl, JsonUtil.toJsonString(payNotifyReq));
		innerCallbackService.postRestTemplate(notifyUrl, payNotifyReq);
	}

	private void financialFlow(Map<String, Object> params, String outTradeNo) {
		// 数据落库, 生成财务流水(入账)
		try {
			String tradeNo = MapUtils.getString(params, "tradeNo");
			String amount = MapUtils.getString(params, "amount");
			String orderPayMethod = MapUtils.getString(params, "orderPayMethod");
			String merchantNo = MapUtils.getString(params, "merchantNo");

			financialFlowService.inAccount(outTradeNo, tradeNo, new BigDecimal(amount),
					OrderPayEnum.Method.of(orderPayMethod), merchantNo);
		} catch (Exception e) {
			log.error("生成财务流水(入账), error: {}", e);
		}
	}
}