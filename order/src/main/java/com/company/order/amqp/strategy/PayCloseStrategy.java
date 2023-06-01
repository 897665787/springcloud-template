package com.company.order.amqp.strategy;

import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.company.common.util.JsonUtil;
import com.company.framework.amqp.BaseStrategy;
import com.company.order.api.enums.OrderPayEnum;
import com.company.order.api.response.PayCloseResp;
import com.company.order.entity.OrderPay;
import com.company.order.pay.PayFactory;
import com.company.order.pay.core.PayClient;
import com.company.order.service.OrderPayService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component(StrategyConstants.PAY_CLOSE_STRATEGY)
public class PayCloseStrategy implements BaseStrategy<Map<String, Object>> {
	@Autowired
	private OrderPayService orderPayService;

	@Override
	public void doStrategy(Map<String, Object> params) {
		String outTradeNo = MapUtils.getString(params, "outTradeNo");
		OrderPay orderPay = orderPayService.selectByOrderCode(outTradeNo);

		PayClient payClient = PayFactory.of(OrderPayEnum.Method.of(orderPay.getMethod()));
		PayCloseResp payCloseResp = payClient.payClose(outTradeNo);// 不管关闭订单结果怎样，都应该当关闭成功处理
		log.info("关闭订单结果:{}", JsonUtil.toJsonString(payCloseResp));
		
		// 修改状态为已关闭
		OrderPay orderPay4Update = new OrderPay();
		orderPay4Update.setStatus(OrderPayEnum.Status.CLOSED.getCode());
		EntityWrapper<OrderPay> wrapper = new EntityWrapper<>();
		wrapper.eq("id", orderPay.getId());
		wrapper.eq("status", OrderPayEnum.Status.WAITPAY.getCode());
		boolean affect = orderPayService.update(orderPay4Update, wrapper);
		if (!affect) {// 更新不成功，说明订单不是处理中状态
			log.info("update订单不是处理中状态，不操作关闭:{}", outTradeNo);
			return;
		}
	}
}
