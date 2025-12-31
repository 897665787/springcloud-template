package com.company.order.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.company.framework.util.Utils;
import com.company.order.api.enums.OrderEnum;
import com.company.order.entity.Order;
import com.company.order.mapper.OrderMapper;
import com.google.common.collect.Lists;

import cn.hutool.core.date.DateUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService extends ServiceImpl<OrderMapper, Order> implements IService<Order> {

	private final OrderMapper orderMapper;

	public List<Order> pageByUserIdAndStatus(Page<Order> page, Integer userId, OrderEnum.StatusEnum status) {
		return orderMapper.pageByUserIdAndStatus(page, userId, status);
	}

	public Order selectByOrderCode(String orderCode) {
		return orderMapper.selectByOrderCode(orderCode);
	}

	public Order saveOrUpdate(Integer userId, String orderType, String orderCode,
			OrderEnum.SubStatusEnum subStatusEnum, BigDecimal productAmount, BigDecimal orderAmount,
			BigDecimal reduceAmount, BigDecimal needPayAmount, String subOrderUrl, String attach) {
		Order order = new Order();
		order.setUserId(userId);
		order.setOrderCode(orderCode);
		order.setOrderType(orderType);
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
		String appendRemark = String.format("取消:%s", DateUtil.formatLocalDateTime(cancelTime));
		return updateStatus(orderCode, order4Update, subStatusEnum,
				Lists.newArrayList(OrderEnum.SubStatusEnum.WAIT_PAY), appendRemark);
	}

	/**
	 * 订单支付成功(支付后订单还没推给商家，为该状态)
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
		String appendRemark = String.format("支付:%s", DateUtil.formatLocalDateTime(payTime));
		return updateStatus(orderCode, order4Update, subStatusEnum,
				Lists.newArrayList(OrderEnum.SubStatusEnum.WAIT_PAY, OrderEnum.SubStatusEnum.CANCELED), appendRemark);
	}

	/**
	 * 待发货 (订单已推给商家，为该状态)
	 *
	 * @param orderCode
	 * @return
	 */
	public boolean waitSend(String orderCode) {
		Order order4Update = new Order();
		OrderEnum.SubStatusEnum subStatusEnum = OrderEnum.SubStatusEnum.WAIT_SEND;
		String appendRemark = String.format("待发货:%s", DateUtil.now());
		return updateStatus(orderCode, order4Update, subStatusEnum, Lists.newArrayList(OrderEnum.SubStatusEnum.WAIT_PAY,
				OrderEnum.SubStatusEnum.CANCELED, OrderEnum.SubStatusEnum.PAYED), appendRemark);
	}

	/**
	 * 发货
	 *
	 * @param orderCode
	 * @return
	 */
	public boolean send(String orderCode) {
		Order order4Update = new Order();
		OrderEnum.SubStatusEnum subStatusEnum = OrderEnum.SubStatusEnum.SEND_SUCCESS;
		String appendRemark = String.format("发货:%s", DateUtil.now());
		return updateStatus(orderCode, order4Update, subStatusEnum, Lists.newArrayList(OrderEnum.SubStatusEnum.WAIT_PAY,
				OrderEnum.SubStatusEnum.CANCELED, OrderEnum.SubStatusEnum.PAYED, OrderEnum.SubStatusEnum.WAIT_SEND),
				appendRemark);
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
		String appendRemark = String.format("收货:%s", DateUtil.formatLocalDateTime(finishTime));
		return updateStatus(orderCode, order4Update, subStatusEnum,
				Lists.newArrayList(OrderEnum.SubStatusEnum.WAIT_PAY, OrderEnum.SubStatusEnum.CANCELED,
						OrderEnum.SubStatusEnum.PAYED, OrderEnum.SubStatusEnum.WAIT_SEND,
						OrderEnum.SubStatusEnum.SEND_SUCCESS),
				appendRemark);
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
		String appendRemark = String.format("完成:%s", DateUtil.formatLocalDateTime(finishTime));
		return updateStatus(orderCode, order4Update, subStatusEnum,
				Lists.newArrayList(OrderEnum.SubStatusEnum.WAIT_PAY, OrderEnum.SubStatusEnum.CANCELED,
						OrderEnum.SubStatusEnum.PAYED, OrderEnum.SubStatusEnum.WAIT_SEND,
						OrderEnum.SubStatusEnum.SEND_SUCCESS, OrderEnum.SubStatusEnum.WAIT_REVIEW),
				appendRemark);
	}

	/**
	 * 退款申请
	 *
	 * @param orderCode
	 * @param refundApplyTime
	 * @return
	 */
	public boolean refundApply(String orderCode, LocalDateTime refundApplyTime) {
		Order order4Update = new Order();
		order4Update.setRefundTime(Optional.ofNullable(refundApplyTime).orElse(LocalDateTime.now()));
		OrderEnum.SubStatusEnum subStatusEnum = OrderEnum.SubStatusEnum.CHECK;
		String appendRemark = String.format("退款申请:%s", DateUtil.formatLocalDateTime(refundApplyTime));
		return updateStatus(orderCode, order4Update, subStatusEnum,
				Lists.newArrayList(OrderEnum.SubStatusEnum.PAYED, OrderEnum.SubStatusEnum.WAIT_SEND,
						OrderEnum.SubStatusEnum.SEND_SUCCESS, OrderEnum.SubStatusEnum.WAIT_REVIEW,
						OrderEnum.SubStatusEnum.COMPLETE),
				appendRemark);
	}

	/**
	 * 退款失败
	 *
	 * @param orderCode
	 * @param oldSubStatus
	 * @param failReason
	 * @return
	 */
	public boolean refundFail(String orderCode, OrderEnum.SubStatusEnum oldSubStatus, String failReason) {
		Order orderDB = orderMapper.selectByOrderCode(orderCode);

		Order order4Update = new Order();
		order4Update.setAttach(Utils.append2Json(orderDB.getAttach(), "failReason", failReason));
		OrderEnum.SubStatusEnum subStatusEnum = oldSubStatus;
		String appendRemark = String.format("退款失败:%s,原状态:%s", failReason, oldSubStatus.getStatus());
		return updateStatus(orderCode, order4Update, subStatusEnum, Lists.newArrayList(OrderEnum.SubStatusEnum.CHECK),
				appendRemark);
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
		String appendRemark = String.format("退款:%s,%s", totalRefundAmount.toPlainString(),
				DateUtil.formatLocalDateTime(refundFinishTime));
		return updateStatus(orderCode, order4Update, subStatusEnum,
				Lists.newArrayList(OrderEnum.SubStatusEnum.WAIT_PAY, OrderEnum.SubStatusEnum.CANCELED,
						OrderEnum.SubStatusEnum.PAYED, OrderEnum.SubStatusEnum.WAIT_SEND,
						OrderEnum.SubStatusEnum.SEND_SUCCESS, OrderEnum.SubStatusEnum.WAIT_REVIEW,
						OrderEnum.SubStatusEnum.COMPLETE, OrderEnum.SubStatusEnum.CHECK),
				appendRemark);
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
	 * @param appendRemark
	 *            追加备注
	 * @return
	 */
	private boolean updateStatus(String orderCode, Order order4Update, OrderEnum.SubStatusEnum newSubStatus,
			List<OrderEnum.SubStatusEnum> conditionSubStatusEnums, String appendRemark) {
		Order orderDB = orderMapper.selectByOrderCode(orderCode);

		OrderEnum.SubStatusEnum oldSubStatus = OrderEnum.SubStatusEnum.of(orderDB.getSubStatus());
		if (conditionSubStatusEnums != null && !conditionSubStatusEnums.contains(oldSubStatus)) {// 条件状态集合，满足条件才能更新成功
			// 只有在条件集合下的状态才能更新
			log.info("{}不是{}状态，当前状态为:{}", orderCode, conditionSubStatusEnums, oldSubStatus);
			return false;
		}

		UpdateWrapper<Order> wrapper = new UpdateWrapper<>();
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
		if (StringUtils.isNotBlank(appendRemark)) {
			order4Update.setRemark(Utils.rightRemark(orderDB.getRemark(), appendRemark));
		}
		order4Update.setUpdateTime(LocalDateTime.now());
		int affect = orderMapper.update(order4Update, wrapper);

		log.info("状态修改:{}|{} -> {}|{},{}", orderDB.getStatus(), orderDB.getSubStatus(), order4Update.getStatus(),
				order4Update.getSubStatus(), affect);

		return affect > 0;
	}

	public boolean deleteOrder(String orderCode) {
		Order order4Update = new Order();
		order4Update.setUserDel(2);
		UpdateWrapper<Order> wrapper = new UpdateWrapper<>();
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
