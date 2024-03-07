package com.company.order.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.company.order.api.enums.OrderEnum;
import com.company.order.entity.Order;
import com.company.order.mapper.OrderMapper;
import com.google.common.collect.Lists;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class OrderService extends ServiceImpl<OrderMapper, Order> implements IService<Order> {

	@Autowired
	private OrderMapper orderMapper;

	public List<Order> pageByUserIdAndStatus(Page<Order> page, Integer userId, OrderEnum.StatusEnum status) {
		return orderMapper.pageByUserIdAndStatus(page, userId, status);
	}

	public Order selectByOrderCode(String orderCode) {
		return orderMapper.selectByOrderCode(orderCode);
	}

	public Order saveOrUpdate(Integer userId, OrderEnum.OrderType orderTypeEnum, String orderCode,
			OrderEnum.SubStatusEnum subStatusEnum, BigDecimal productAmount, BigDecimal orderAmount,
			BigDecimal reduceAmount, BigDecimal needPayAmount, String subOrderUrl, String attach) {
		Order order = new Order();
		order.setUserId(userId);
		order.setOrderCode(orderCode);
		order.setOrderType(orderTypeEnum.getCode());
		order.setStatus(OrderEnum.SubStatusEnum.toStatusEnum(subStatusEnum).getStatus());
		order.setSubStatus(subStatusEnum.getStatus());

		order.setProductAmount(productAmount);
		order.setOrderAmount(orderAmount);
		order.setReduceAmount(reduceAmount);
		order.setNeedPayAmount(needPayAmount);
		order.setSubOrderUrl(subOrderUrl);
		order.setAttach(attach);

		orderMapper.saveOrUpdate(order);// 正常情况下都是save，为了避免极端情况下不影响业务功能加了update
		return order;
	}

	public int cancel(String orderCode, LocalDateTime cancelTime) {
		Order order4Update = new Order();
		order4Update.setPayTime(cancelTime);
		OrderEnum.SubStatusEnum subStatusEnum = OrderEnum.SubStatusEnum.CANCELED;
		return updateStatus(orderCode, order4Update, subStatusEnum,
				Lists.newArrayList(OrderEnum.SubStatusEnum.WAIT_PAY));
	}

	public int paySuccess(String orderCode, LocalDateTime payTime) {
		Order order4Update = new Order();
		order4Update.setPayTime(payTime);
		OrderEnum.SubStatusEnum subStatusEnum = OrderEnum.SubStatusEnum.PAYED;
		return updateStatus(orderCode, order4Update, subStatusEnum,
				Lists.newArrayList(OrderEnum.SubStatusEnum.WAIT_PAY, OrderEnum.SubStatusEnum.CANCELED));
	}

	public int finish(String orderCode, LocalDateTime finishTime) {
		Order order4Update = new Order();
		order4Update.setFinishTime(finishTime);
		OrderEnum.SubStatusEnum subStatusEnum = OrderEnum.SubStatusEnum.COMPLETE;
		return updateStatus(orderCode, order4Update, subStatusEnum,
				Lists.newArrayList(OrderEnum.SubStatusEnum.WAIT_PAY, OrderEnum.SubStatusEnum.CANCELED,
						OrderEnum.SubStatusEnum.PAYED, OrderEnum.SubStatusEnum.WAIT_SEND,
						OrderEnum.SubStatusEnum.SENDING, OrderEnum.SubStatusEnum.SEND_FAIL,
						OrderEnum.SubStatusEnum.SEND_SUCCESS, OrderEnum.SubStatusEnum.WAIT_REVIEW));
	}

	/**
	 * 更新状态
	 * 
	 * @param orderCode
	 *            订单号
	 * @param order4Update
	 *            更新对象信息
	 * @param subStatusEnum
	 *            子状态
	 * @param conditionSubStatusEnums
	 *            条件状态，为null表示不加条件
	 * @return
	 */
	private int updateStatus(String orderCode, Order order4Update, OrderEnum.SubStatusEnum subStatusEnum,
			List<OrderEnum.SubStatusEnum> conditionSubStatusEnums) {
		Order orderDB = orderMapper.selectByOrderCode(orderCode);
		if (orderDB == null) {
			log.warn("订单不存在:{}", orderCode);
			return 0;
		}

		OrderEnum.SubStatusEnum subStatus = OrderEnum.SubStatusEnum.of(orderDB.getSubStatus());
		if (conditionSubStatusEnums != null && !conditionSubStatusEnums.contains(subStatus)) {// 条件状态集合，满足条件才能更新成功
			// 只有在条件集合下的状态才能更新
			log.info("{}不是{}状态，当前状态为:{}", orderCode, conditionSubStatusEnums, subStatus);
			return 0;
		}

		EntityWrapper<Order> wrapper = new EntityWrapper<>();
		wrapper.eq("order_code", orderCode);

		if (conditionSubStatusEnums != null) {
			if (conditionSubStatusEnums.isEmpty()) {// 条件状态集合，满足条件才能更新成功
				wrapper.eq("sub_status", -1);// -1无意义
			} else {
				List<Integer> subStatusList = conditionSubStatusEnums.stream().map(OrderEnum.SubStatusEnum::getStatus)
						.collect(Collectors.toList());
				wrapper.in("sub_status", subStatusList);
			}
		}
		order4Update.setStatus(OrderEnum.SubStatusEnum.toStatusEnum(subStatusEnum).getStatus());
		order4Update.setSubStatus(subStatusEnum.getStatus());
		order4Update.setUpdateTime(LocalDateTime.now());
		int affect = orderMapper.update(order4Update, wrapper);

		log.info("状态修改:{}|{} -> {}|{},{}", orderDB.getStatus(), orderDB.getSubStatus(), order4Update.getStatus(),
				order4Update.getSubStatus(), affect);

		return affect;
	}

}
