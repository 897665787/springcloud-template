package com.company.order.api.request;

import javax.validation.constraints.NotNull;

import com.company.order.api.enums.OrderEnum;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * <p>
 * 客户订单表的更新请求参数
 * </p>
 *
 * @author sys
 * @since 2020-04-20
 */
@Data
@Accessors(chain = true)
public class ChangeOrderStatusReq {
	/**
	 * 订单号 关联对应业务订单表的订单号
	 */
	@NotNull(message = "订单号不能为空")
	private String orderCode;

	/**
	 * 订单类型
	 */
	@NotNull(message = "订单类型不能为空")
	private OrderEnum.OrderType orderTypeEnum;

	/**
	 * 订单下一个子状态
	 */
	@NotNull(message = "订单下一个子状态")
	private OrderEnum.SubStatusEnum targetSubStatusEnum;
}
