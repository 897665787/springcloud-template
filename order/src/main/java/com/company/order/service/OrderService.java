package com.company.order.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.company.common.exception.BusinessException;
import com.company.common.util.Utils;
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

	/**
	 * 订单取消
	 * 
	 * @param orderCode
	 * @param cancelTime
	 * @return
	 */
	public boolean cancel(String orderCode, LocalDateTime cancelTime) {
		Order order4Update = new Order();
		order4Update.setPayTime(Optional.ofNullable(cancelTime).orElse(LocalDateTime.now()));
		OrderEnum.SubStatusEnum subStatusEnum = OrderEnum.SubStatusEnum.CANCELED;
		return updateStatus(orderCode, order4Update, subStatusEnum,
				Lists.newArrayList(OrderEnum.SubStatusEnum.WAIT_PAY));
	}

	/**
	 * 订单支付成功
	 * 
	 * @param orderCode
	 * @param payAmount
	 * @param payTime
	 * @return
	 */
	public boolean paySuccess(String orderCode, BigDecimal payAmount, LocalDateTime payTime) {
		Order order4Update = new Order();
		order4Update.setPayAmount(payAmount);
		order4Update.setPayTime(Optional.ofNullable(payTime).orElse(LocalDateTime.now()));
		OrderEnum.SubStatusEnum subStatusEnum = OrderEnum.SubStatusEnum.PAYED;
		return updateStatus(orderCode, order4Update, subStatusEnum,
				Lists.newArrayList(OrderEnum.SubStatusEnum.WAIT_PAY, OrderEnum.SubStatusEnum.CANCELED));
	}

	/**
	 * 确认收货
	 * 
	 * @param orderCode
	 * @return
	 */
	public boolean receive(String orderCode, LocalDateTime finishTime) {
		Order order4Update = new Order();
		order4Update.setFinishTime(Optional.ofNullable(finishTime).orElse(LocalDateTime.now()));
		OrderEnum.SubStatusEnum subStatusEnum = OrderEnum.SubStatusEnum.WAIT_REVIEW;
		return updateStatus(orderCode, order4Update, subStatusEnum,
				Lists.newArrayList(OrderEnum.SubStatusEnum.WAIT_PAY, OrderEnum.SubStatusEnum.CANCELED,
						OrderEnum.SubStatusEnum.PAYED, OrderEnum.SubStatusEnum.WAIT_SEND,
						OrderEnum.SubStatusEnum.SENDING, OrderEnum.SubStatusEnum.SEND_FAIL,
						OrderEnum.SubStatusEnum.SEND_SUCCESS));
	}

	/**
	 * 订单完成
	 * 
	 * @param orderCode
	 * @param finishTime
	 * @return
	 */
	public boolean finish(String orderCode, LocalDateTime finishTime) {
		Order order4Update = new Order();
		order4Update.setFinishTime(Optional.ofNullable(finishTime).orElse(LocalDateTime.now()));
		OrderEnum.SubStatusEnum subStatusEnum = OrderEnum.SubStatusEnum.COMPLETE;
		return updateStatus(orderCode, order4Update, subStatusEnum,
				Lists.newArrayList(OrderEnum.SubStatusEnum.WAIT_PAY, OrderEnum.SubStatusEnum.CANCELED,
						OrderEnum.SubStatusEnum.PAYED, OrderEnum.SubStatusEnum.WAIT_SEND,
						OrderEnum.SubStatusEnum.SENDING, OrderEnum.SubStatusEnum.SEND_FAIL,
						OrderEnum.SubStatusEnum.SEND_SUCCESS, OrderEnum.SubStatusEnum.WAIT_REVIEW));
	}
	
	/**
	 * 退款申请
	 * 
	 * @param orderCode
	 * @param refundApplyTime
	 * @return
	 */
	public OrderEnum.SubStatusEnum refundApply(String orderCode, LocalDateTime refundApplyTime) {
		Order orderDB = orderMapper.selectByOrderCode(orderCode);

		OrderEnum.SubStatusEnum oldSubStatus = OrderEnum.SubStatusEnum.of(orderDB.getSubStatus());

		OrderEnum.SubStatusEnum newSubStatus = OrderEnum.SubStatusEnum.CHECK;
		List<OrderEnum.SubStatusEnum> conditionSubStatusEnums = Lists.newArrayList(OrderEnum.SubStatusEnum.PAYED,
				OrderEnum.SubStatusEnum.WAIT_SEND, OrderEnum.SubStatusEnum.SENDING, OrderEnum.SubStatusEnum.SEND_FAIL,
				OrderEnum.SubStatusEnum.SEND_SUCCESS, OrderEnum.SubStatusEnum.WAIT_REVIEW,
				OrderEnum.SubStatusEnum.COMPLETE);

		if (conditionSubStatusEnums != null && !conditionSubStatusEnums.contains(oldSubStatus)) {// 条件状态集合，满足条件才能更新成功
			// 只有在条件集合下的状态才能更新
			log.warn("{}不是{}状态，当前状态为:{}", orderCode, conditionSubStatusEnums, oldSubStatus);
			throw new BusinessException("当前不可申请退款，请刷新后重试！");
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
		Order order4Update = new Order();
		order4Update.setRefundTime(Optional.ofNullable(refundApplyTime).orElse(LocalDateTime.now()));
		order4Update.setStatus(OrderEnum.SubStatusEnum.toStatusEnum(newSubStatus).getStatus());
		order4Update.setSubStatus(newSubStatus.getStatus());
		order4Update.setUpdateTime(LocalDateTime.now());
		int affect = orderMapper.update(order4Update, wrapper);

		log.info("状态修改:{}|{} -> {}|{},{}", orderDB.getStatus(), orderDB.getSubStatus(), order4Update.getStatus(),
				order4Update.getSubStatus(), affect);

		if (affect == 0) {
			orderDB = orderMapper.selectByOrderCode(orderCode);
			oldSubStatus = OrderEnum.SubStatusEnum.of(orderDB.getSubStatus());
			log.info("{}不是{}状态，当前状态为:{}", orderCode, conditionSubStatusEnums, oldSubStatus);
			throw new BusinessException("当前不可申请退款，请刷新后重试！");
		}
		return oldSubStatus;
	}
	
	/**
	 * 退款审核拒绝
	 * 
	 * @param orderCode
	 * @param oldSubStatus
	 * @param rejectReason
	 * @return
	 */
	public boolean refundReject(String orderCode, OrderEnum.SubStatusEnum oldSubStatus, String rejectReason) {
		Order orderDB = orderMapper.selectByOrderCode(orderCode);
		String attach = orderDB.getAttach();
		attach = Utils.append2Json(attach, "rejectReason", rejectReason);
		
		Order order4Update = new Order();
		order4Update.setAttach(attach);
		OrderEnum.SubStatusEnum subStatusEnum = oldSubStatus;
		return updateStatus(orderCode, order4Update, subStatusEnum,
				Lists.newArrayList(OrderEnum.SubStatusEnum.CHECK, OrderEnum.SubStatusEnum.REFUNDING));
	}
	
	/**
	 * 退款完成
	 * 
	 * @param orderCode
	 * @param refundFinishTime
	 * @param totalRefundAmount
	 * @param refundAll
	 * @return
	 */
	public boolean refundFinish(String orderCode, LocalDateTime refundFinishTime, BigDecimal totalRefundAmount,
			boolean refundAll) {
		Order order4Update = new Order();
		order4Update.setRefundAmount(totalRefundAmount);
		order4Update.setRefundTime(Optional.ofNullable(refundFinishTime).orElse(LocalDateTime.now()));
		OrderEnum.SubStatusEnum subStatusEnum = OrderEnum.SubStatusEnum.REFUND_SUCCESS;// 全额退款
		if (!refundAll) {
			subStatusEnum = OrderEnum.SubStatusEnum.COMPLETE;// 部分退款
		}
		return updateStatus(orderCode, order4Update, subStatusEnum, Lists.newArrayList(OrderEnum.SubStatusEnum.PAYED,
				OrderEnum.SubStatusEnum.WAIT_SEND, OrderEnum.SubStatusEnum.SENDING, OrderEnum.SubStatusEnum.SEND_FAIL,
				OrderEnum.SubStatusEnum.SEND_SUCCESS, OrderEnum.SubStatusEnum.WAIT_REVIEW,
				OrderEnum.SubStatusEnum.COMPLETE, OrderEnum.SubStatusEnum.CHECK, OrderEnum.SubStatusEnum.REFUNDING));
	}

	/**
	 * 更新状态
	 * 
	 * @param orderCode
	 *            订单号
	 * @param order4Update
	 *            更新对象信息
	 * @param newSubStatus
	 *            子状态
	 * @param conditionSubStatusEnums
	 *            条件状态，为null表示不加条件
	 * @return
	 */
	private boolean updateStatus(String orderCode, Order order4Update, OrderEnum.SubStatusEnum newSubStatus,
			List<OrderEnum.SubStatusEnum> conditionSubStatusEnums) {
		Order orderDB = orderMapper.selectByOrderCode(orderCode);

		OrderEnum.SubStatusEnum oldSubStatus = OrderEnum.SubStatusEnum.of(orderDB.getSubStatus());
		if (conditionSubStatusEnums != null && !conditionSubStatusEnums.contains(oldSubStatus)) {// 条件状态集合，满足条件才能更新成功
			// 只有在条件集合下的状态才能更新
			log.info("{}不是{}状态，当前状态为:{}", orderCode, conditionSubStatusEnums, oldSubStatus);
			return false;
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
		order4Update.setStatus(OrderEnum.SubStatusEnum.toStatusEnum(newSubStatus).getStatus());
		order4Update.setSubStatus(newSubStatus.getStatus());
		order4Update.setUpdateTime(LocalDateTime.now());
		int affect = orderMapper.update(order4Update, wrapper);

		log.info("状态修改:{}|{} -> {}|{},{}", orderDB.getStatus(), orderDB.getSubStatus(), order4Update.getStatus(),
				order4Update.getSubStatus(), affect);

		return affect > 0;
	}

	public boolean deleteOrder(String orderCode) {
		Order order4Update = new Order();
		order4Update.setUserDel(2);
		EntityWrapper<Order> wrapper = new EntityWrapper<>();
		wrapper.eq("order_code", orderCode);
		wrapper.eq("user_del", 1);
		int affect = orderMapper.update(order4Update, wrapper);
		return affect > 0;
	}
	
	public List<String> select4OverSendSuccess(Integer limit) {
		LocalDateTime payTimeBegin = LocalDateTime.now().minusDays(5);// 发货n天后收货的订单
		return orderMapper.select4OverSendSuccess(payTimeBegin, OrderEnum.SubStatusEnum.SEND_SUCCESS, limit);
	}

	public List<String> select4OverWaitReview(Integer limit) {
		LocalDateTime finishTimeBegin = LocalDateTime.now().minusDays(7);// 完成n天后没有评价的订单
		return orderMapper.select4OverWaitReview(finishTimeBegin, OrderEnum.SubStatusEnum.WAIT_REVIEW, limit);
	}
}
