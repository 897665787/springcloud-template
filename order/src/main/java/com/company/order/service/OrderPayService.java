package com.company.order.service;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.company.order.entity.OrderPay;
import com.company.order.mapper.OrderPayMapper;

@Service
public class OrderPayService extends ServiceImpl<OrderPayMapper, OrderPay>  {

    public OrderPay selectByOrderCode(String orderCode) {
        return this.baseMapper.selectByOrderCode(orderCode);
    }
}
