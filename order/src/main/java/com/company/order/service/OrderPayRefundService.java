package com.company.order.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.company.order.entity.OrderPayRefund;
import com.company.order.mapper.OrderPayRefundMapper;

@Service
public class OrderPayRefundService extends ServiceImpl<OrderPayRefundMapper, OrderPayRefund>  {

    public OrderPayRefund selectByRefundOrderCode(String refundOrderCode) {
        return this.baseMapper.selectByRefundOrderCode(refundOrderCode);
    }

	public BigDecimal sumRefundAmount(String orderCode) {
		BigDecimal sumRefundAmount = baseMapper.sumRefundAmount(orderCode);
		return Optional.ofNullable(sumRefundAmount).orElse(BigDecimal.ZERO);
	}

	public List<OrderPayRefund> listByOrderCode(String orderCode) {
		return baseMapper.listByOrderCode(orderCode);
	}
}
