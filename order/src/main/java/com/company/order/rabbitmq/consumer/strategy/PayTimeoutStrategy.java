package com.company.order.rabbitmq.consumer.strategy;

import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.company.common.util.JsonUtil;
import com.company.framework.amqp.rabbit.BaseStrategy;
import com.company.order.api.enums.OrderPayEnum;
import com.company.order.api.request.PayNotifyReq;
import com.company.order.api.response.PayCloseResp;
import com.company.order.api.response.PayTradeStateResp;
import com.company.order.entity.OrderPay;
import com.company.order.innercallback.service.IInnerCallbackService;
import com.company.order.pay.PayFactory;
import com.company.order.pay.core.PayClient;
import com.company.order.service.OrderPayService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component(StrategyConstants.PAY_TIMEOUT_STRATEGY)
public class PayTimeoutStrategy implements BaseStrategy<Map<String, Object>> {

	@Autowired
	private OrderPayService orderPayService;
	@Autowired
	private IInnerCallbackService innerCallbackService;
    
	@Override
	public void doStrategy(Map<String, Object> params) {
		String orderCode = MapUtils.getString(params, "orderCode");

		OrderPay orderPay = orderPayService.selectByOrderCode(orderCode);
		if (orderPay == null) {
			log.info("订单不存在:{}", orderCode);
			return;
		}

		OrderPayEnum.Status status = OrderPayEnum.Status.of(orderPay.getStatus());
		if (status != OrderPayEnum.Status.WAITPAY) {
			log.info("订单不是处理中状态，不允许关闭:{}，当前状态为:{}", orderCode, status);
			return;
		}
		
		PayClient payClient = PayFactory.of(OrderPayEnum.Method.of(orderPay.getMethod()));
		PayTradeStateResp payTradeState = payClient.queryTradeState(orderCode);
		if (payTradeState.getResult() && payTradeState.getPaySuccess()) {
			// 支付成功的情况下不能关闭订单
			log.info("订单{}支付成功，但未回调", orderCode);
			// TODO 是否需要回调/server/callback/wechat 或 /server/callback/ali 或直接回调到PAY_NOTIFY_STRATEGY
			return;
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
			return;
		}
		
		// 超时未支付关闭订单
		PayCloseResp payCloseResp = payClient.payClose(orderCode);// 不管关闭订单结果怎样，都应该当关闭成功处理
		log.info("关闭订单结果:{}", JsonUtil.toJsonString(payCloseResp));
        
		String notifyUrl = orderPay.getNotifyUrl();
		if (StringUtils.isBlank(notifyUrl)) {
			log.info("无回调URL");
			return;
		}

		// 回调超时未支付关闭订单到对应业务中
		PayNotifyReq payNotifyReq = new PayNotifyReq();
		payNotifyReq.setEvent(PayNotifyReq.EVENT.CLOSE);
		payNotifyReq.setSuccess(false);// 返回false避免出现超时回调认为是支付成功回调
		String message = "超时未支付关闭订单";
		payNotifyReq.setMessage(message);
		payNotifyReq.setOrderCode(orderCode);
		payNotifyReq.setAttach(orderPay.getNotifyAttach());

		log.info("超时未支付关闭订单回调,请求地址:{},参数:{}", notifyUrl, JsonUtil.toJsonString(payNotifyReq));
		innerCallbackService.postRestTemplate(notifyUrl, payNotifyReq);
	}
}
