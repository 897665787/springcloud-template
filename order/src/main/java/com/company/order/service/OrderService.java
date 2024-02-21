package com.company.order.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.company.common.util.Utils;
import com.company.order.api.enums.OrderEnum;
import com.company.order.entity.Order;
import com.company.order.entity.OrderProduct;
import com.company.order.mapper.OrderMapper;
import com.company.order.mapper.OrderProductMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class OrderService extends ServiceImpl<OrderMapper, Order> implements IService<Order> {

	@Autowired
	private OrderMapper orderMapper;
	@Autowired
	private OrderProductMapper orderProductMapper;

	public List<Order> selectByUserIdAndStatus(Page<Order> page, Integer userId, OrderEnum.StatusEnum status) {
		return orderMapper.selectByUserIdAndStatus(page, userId, status);
	}

	public Order selectByOrderCode(String orderCode) {
		return orderMapper.selectByOrderCode(orderCode);
	}

	public Order saveOrUpdate(Integer userId, OrderEnum.OrderType orderTypeEnum, String orderCode,
			OrderEnum.SubStatusEnum subStatusEnum, BigDecimal productAmount, BigDecimal orderAmount,
			BigDecimal reduceAmount, BigDecimal needPayAmount, String subOrderUrl) {
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

		orderMapper.saveOrUpdate(order);// 正常情况下都是save，为了避免极端情况下不影响业务功能加了update
		return order;
	}

	public Integer updateStatus(String orderCode, OrderEnum.SubStatusEnum subStatusEnum) {
		Order order4Update = new Order();
		return updateStatus(orderCode, order4Update, subStatusEnum);
	}

	public Integer paySuccess(String orderCode) {
		Order order4Update = new Order();
		order4Update.setPayTime(LocalDateTime.now());
		OrderEnum.SubStatusEnum subStatusEnum = OrderEnum.SubStatusEnum.PAYED;
		return updateStatus(orderCode, order4Update, subStatusEnum);
	}

	public Integer finish(String orderCode) {
		Order order4Update = new Order();
		order4Update.setFinishTime(LocalDateTime.now());
		OrderEnum.SubStatusEnum subStatusEnum = OrderEnum.SubStatusEnum.COMPLETE;
		return updateStatus(orderCode, order4Update, subStatusEnum);
	}

	public Integer finish(String orderCode, LocalDateTime finishTime) {
		Order order4Update = new Order();
		order4Update.setFinishTime(finishTime);
		OrderEnum.SubStatusEnum subStatusEnum = OrderEnum.SubStatusEnum.COMPLETE;
		return updateStatus(orderCode, order4Update, subStatusEnum);
	}

	private Integer updateStatus(String orderCode, Order order4Update, OrderEnum.SubStatusEnum subStatusEnum) {
		Order orderDB = orderMapper.selectByOrderCode(orderCode);
		if (orderDB == null) {
			log.warn("订单不存在:{}", orderCode);
			return 0;
		}

		EntityWrapper<Order> wrapper = new EntityWrapper<>();
		wrapper.eq("order_code", orderCode);
		order4Update.setStatus(OrderEnum.SubStatusEnum.toStatusEnum(subStatusEnum).getStatus());
		order4Update.setSubStatus(subStatusEnum.getStatus());
		order4Update.setUpdateTime(LocalDateTime.now());
		int affect = orderMapper.update(order4Update, wrapper);

		log.info("状态修改:{}|{} -> {}|{},{}", orderDB.getStatus(), orderDB.getSubStatus(), order4Update.getStatus(),
				order4Update.getSubStatus(), affect);

		return affect;
	}

	public Integer cancel(String orderCode) {
		Order orderDB = orderMapper.selectByOrderCode(orderCode);
		if (orderDB == null) {
			log.warn("订单不存在:{}", orderCode);
			return 0;
		}

		if (OrderEnum.StatusEnum.WAIT_PAY != OrderEnum.StatusEnum.of(orderDB.getStatus())) {
			// 只有待付款状态才可以取消订单
			log.info("{}不是待付款状态，不能取消，当前状态为:{}", orderCode, OrderEnum.StatusEnum.of(orderDB.getStatus()).getMessage());
			return 0;
		}

		Order order4Update = new Order();
		order4Update.setPayTime(LocalDateTime.now());
		OrderEnum.SubStatusEnum subStatusEnum = OrderEnum.SubStatusEnum.CANCELED;

		EntityWrapper<Order> wrapper = new EntityWrapper<>();
		wrapper.eq("order_code", orderCode);
		wrapper.eq("status", OrderEnum.StatusEnum.WAIT_PAY.getStatus());// 只有待付款状态才可以取消订单
		order4Update.setStatus(OrderEnum.SubStatusEnum.toStatusEnum(subStatusEnum).getStatus());
		order4Update.setSubStatus(subStatusEnum.getStatus());
		order4Update.setUpdateTime(LocalDateTime.now());
		int affect = orderMapper.update(order4Update, wrapper);

		log.info("状态修改:{}|{} -> {}|{},{}", orderDB.getStatus(), orderDB.getSubStatus(), order4Update.getStatus(),
				order4Update.getSubStatus(), affect);

//		// 库存回滚
//		if (affect > 0) {
//			OrderEnum.OrderType businessType = OrderEnum.OrderType.of(orderDB.getBusinessType());
//			OrderTool orderTool = OrderToolBeanFactory.fromBusinessType(businessType);
//			orderTool.rollbackInventory(orderDB);
//
//			// 处理业务逻辑
//			orderTool.handleCancelOrderBusiness(orderDB);
//		}
//
//		if (affect > 0) {
//			UserOrderData userOrderData = OrderToolBeanFactory
//					.fromBusinessType(OrderEnum.OrderType.of(orderDB.getBusinessType())).getBusiness(orderCode);
//			// 修改‘我的订单’订单状态
//			UserOrderUpdateStatusReq userOrderUpdateStatusReq = new UserOrderUpdateStatusReq();
//			userOrderUpdateStatusReq.setBusinessId(userOrderData.getBusinessId());
//			userOrderUpdateStatusReq.setBusinessTypeEnum(userOrderData.getBusinessType());
//			userOrderUpdateStatusReq.setTargetSubStatusEnum(UserOrderEnum.SubStatusEnum.CANCELED);
//			userOrderServiceFeign.changeStatus(userOrderUpdateStatusReq).parseDataThrow();
//		}
		return affect;
	}

	public int payFail(String orderCode, String remark) {
		// 支付失败
		Order orderDB = this.selectByOrderCode(orderCode);
		Order order4Update = new Order();
		order4Update.setId(orderDB.getId());
		order4Update.setRemark(Utils.appendRemark(orderDB.getRemark(), remark));
		int affect = orderMapper.updateById(order4Update);

//		if (affect > 0) {
//			OrderEnum.OrderType businessType = OrderEnum.OrderType.of(orderDB.getBusinessType());
//			if (businessType != null) {
//				OrderTool orderTool = OrderToolBeanFactory.fromBusinessType(businessType);
//				orderTool.handlePayFailBusiness(orderCode);
//			}
//		}
		return affect;
	}

	public List<Order> selectUnFinishOrder(Integer userId) {
		return baseMapper.selectUnFinishOrder(userId);
	}

	public List<Order> selectUnfinishedGroupMealOrder(Integer userId) {
		return baseMapper.selectUnfinishedGroupMealOrder(userId);
	}

	public Order selectNewestMealSuccessOrder4GroupMeal(Integer userId) {
		return baseMapper.selectNewestMealSuccessOrder4GroupMeal(userId);
	}

	public Boolean judgeIsNewPerson(Integer userId, String businessType) {
		Order order = orderMapper.selectIsNewPerson(userId, businessType);
		return order == null;
	}

	public List<Order> selectByOrderCodesAndStatus(Set<String> orderCodeSet, Set<Integer> statusSet) {
		return orderMapper.selectByOrderCodesAndStatus(orderCodeSet, statusSet);
	}

	public List<Order> selectByOrderCodeList(List<String> orderCodeList) {
		if (CollectionUtils.isEmpty(orderCodeList)) {
			return new ArrayList<>();
		}
		return baseMapper.selectList(new EntityWrapper<Order>().in("order_code", orderCodeList).eq("del_flag", "0"));
	}

	public List<OrderProduct> selectOrderProductByOrderCodeList(List<String> orderCodeList) {
		if (CollectionUtils.isEmpty(orderCodeList)) {
			return new ArrayList<>();
		}
		return orderProductMapper
				.selectList(new EntityWrapper<OrderProduct>().in("order_code", orderCodeList).eq("del_flag", "0"));
	}

	public Integer countGroupMealCompleted(Integer userId) {
		return selectCount(new EntityWrapper<Order>().eq("app_user_id", userId)
				.eq("business_type", OrderEnum.OrderType.GROUP_MEAL.getCode())
				.eq("status", OrderEnum.StatusEnum.COMPLETE.getStatus()).eq("del_flag", "0"));
	}

	public Date lastOrderTime(Integer userId) {
		return baseMapper.lastOrderTime(userId);
	}

	public Order selectFirstOrder(Integer userId) {
		return baseMapper.selectFirstOrder(userId);
	}
}
