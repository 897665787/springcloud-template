package com.company.order.service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.company.order.entity.OrderProduct;
import com.company.order.mapper.OrderProductMapper;

@Service
public class OrderProductService extends ServiceImpl<OrderProductMapper, OrderProduct>
		implements IService<OrderProduct> {

	public List<OrderProduct> selectByOrderCode(String orderCode) {
		return baseMapper.selectByOrderCode(orderCode);
	}

	public List<OrderProduct> selectByOrderCodes(List<String> orderCodes) {
		if (CollectionUtils.isEmpty(orderCodes)) {
			return Collections.emptyList();
		}
		return baseMapper.selectByOrderCodes(orderCodes);
	}

	public Map<String, List<OrderProduct>> groupByOrderCodes(List<String> orderCodeList) {
		List<OrderProduct> orderProductList = selectByOrderCodes(orderCodeList);
		Map<String, List<OrderProduct>> orderCodeThisListMap = orderProductList.stream()
				.collect(Collectors.groupingBy(OrderProduct::getOrderCode));
		return orderCodeThisListMap;
	}

}
