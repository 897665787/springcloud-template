package com.company.order.service;

import java.math.BigDecimal;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.company.order.api.enums.OrderPayEnum;
import com.company.order.entity.FinancialFlow;
import com.company.order.entity.OrderPay;
import com.company.order.mapper.FinancialFlowMapper;
import com.company.order.mapper.OrderPayMapper;

@Service
@RequiredArgsConstructor
public class FinancialFlowService extends ServiceImpl<FinancialFlowMapper, FinancialFlow> {

	private final OrderPayMapper orderPayMapper;
	
	public void mchOutAccount(String orderCode, String tradeNo, BigDecimal amount, OrderPayEnum.Method tradeWayEnum,
			String merchantNo) {
		OrderPay orderPay = orderPayMapper.selectByOrderCode(orderCode);
		orderPay.getAmount();
		OrderPayEnum.BusinessType businessType = OrderPayEnum.BusinessType.of(orderPay.getBusinessType());
		saveFinancialFlow(orderCode, tradeNo, amount.negate(), tradeWayEnum, merchantNo, businessType);
	}

	public void outAccount(String outTradeNo, String orderCode, String tradeNo, BigDecimal amount,
			OrderPayEnum.Method tradeWayEnum, String merchantNo) {

		FinancialFlow financialFlow = baseMapper.selectByOrderCode(orderCode);
		if (financialFlow != null) {
			return;
		}

		OrderPay orderPay = orderPayMapper.selectByOrderCode(orderCode);
		OrderPayEnum.BusinessType businessType = OrderPayEnum.BusinessType.of(orderPay.getBusinessType());
		
		financialFlow = new FinancialFlow().setOrderCode(orderCode).setTradeNo(tradeNo).setAmount(amount.negate())
				.setBusinessSource(businessType.getCode()).setTradeWay(tradeWayEnum.getCode()).setMerchantNo(merchantNo)
				// 退款的出账，把原订单号写到remarks字段，以便需要查退的哪笔订单
				.setRemark(outTradeNo);

		baseMapper.insert(financialFlow);
	}

	public void inAccount(String orderCode, String tradeNo, BigDecimal amount, OrderPayEnum.Method tradeWayEnum,
			String merchantNo) {
		OrderPay orderPay = orderPayMapper.selectByOrderCode(orderCode);
		OrderPayEnum.BusinessType businessType = OrderPayEnum.BusinessType.of(orderPay.getBusinessType());
		
		saveFinancialFlow(orderCode, tradeNo, amount, tradeWayEnum, merchantNo, businessType);
	}

	private void saveFinancialFlow(String orderCode, String tradeNo, BigDecimal amount,
			OrderPayEnum.Method tradeWayEnum, String merchantNo, OrderPayEnum.BusinessType businessType) {
		FinancialFlow financialFlow = baseMapper.selectByOrderCode(orderCode);
		if (financialFlow != null) {
			return;
		}

		financialFlow = new FinancialFlow().setOrderCode(orderCode).setTradeNo(tradeNo).setAmount(amount)
				.setBusinessSource(businessType.getCode()).setTradeWay(tradeWayEnum.getCode())
				.setMerchantNo(merchantNo);

		baseMapper.insert(financialFlow);

	}
}
