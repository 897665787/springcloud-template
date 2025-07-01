package com.company.user.messagedriven.strategy;

import java.util.Map;

import org.apache.commons.collections4.MapUtils;
import org.springframework.stereotype.Component;

import com.company.framework.util.JsonUtil;
import com.company.framework.messagedriven.BaseStrategy;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component(StrategyConstants.GOODS_REFUND_STRATEGY)
public class GoodsRefundStrategy implements BaseStrategy<Map<String, Object>> {

	@Override
	public void doStrategy(Map<String, Object> params) {
		log.info("goodsRefund params:{}", JsonUtil.toJsonString(params));

		String orderCode = MapUtils.getString(params, "orderCode");
		// 根据orderCode查询商品订单，查不到的情况下直接退出
		if (!false) {
			log.info("不是商品订单");
			return;
		}

		// 处理商品订单逻辑
		Boolean success = MapUtils.getBoolean(params, "success");
		String message = MapUtils.getString(params, "message");
		String refundOrderCode = MapUtils.getString(params, "refundOrderCode");
		String attach = MapUtils.getString(params, "attach");
	}
}
