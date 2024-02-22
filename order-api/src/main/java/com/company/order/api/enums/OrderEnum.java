package com.company.order.api.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

public interface OrderEnum {
	// 订单类型order_type
	@AllArgsConstructor
    enum OrderType {
		BUY_MEMBER("buy_member", "配送"),
		DISTRIBUTE("distribute", "配送"),
		WRITEOFF("writeoff", "核销码"),
		GROUP_MEAL("group_meal", "团餐"),
		ENT_GROUP_MEAL("ent_group_meal", "企业团餐"),
		GROUP_MEAL_COUPON("group_meal_coupon", "外卖团购券"),
		GROUP_MEAL_HELP_BUY("group_meal_help_buy", "外卖助力")
		;
		@Getter
		private String code;
		@Getter
		private String desc;

		public static OrderType of(String code) {
			for (OrderType item : OrderType.values()) {
				if (item.getCode().equals(code)) {
					return item;
				}
			}
			return null;
		}
	}

	// 主状态status
	@AllArgsConstructor
    enum StatusEnum {
        WAIT_PAY(1, "待付款"),// 待支付
        CANCELED(2, "已关闭"),// 已取消 END
        WAIT_SEND(3, "已付款"),//(待发货)
        WAIT_RECEIVE(4, "待收货"),
        COMPLETE(5, "已完成"),
        REFUND(6, "退款")
        ;

		@Getter
		private Integer status;
		@Getter
		private String message;

		public static StatusEnum of(Integer status) {
			for (StatusEnum item : StatusEnum.values()) {
				if (item.getStatus().equals(status)) {
					return item;
				}
			}
			return null;
		}

    }

	// 子状态sub_status
	@AllArgsConstructor
    enum SubStatusEnum {
        WAIT_PAY(11, "待付款"),// 待支付
        CANCELED(21, "已关闭"),// 已取消 END
        PAYED(30, "已付款"),
        WAIT_SEND(31, "待发货"),
        SENDING(32, "发货中"),
        SEND_FAIL(33, "发货失败"),
        SEND_SUCCESS(41, "已发货"),
        WAIT_REVIEW(51, "待评价"),
		COMPLETE(52, "已结束"), // END
		CHECK(60, "退款审核中"),//退款待审核
        REFUNDING(61, "退款中"),
        REFUND_SUCCESS(62, "退款成功"),// END
        REFUND_FAIL(63, "退款失败")
        ;

		@Getter
		private Integer status;
		@Getter
		private String message;

		public static SubStatusEnum of(Integer status) {
			for (SubStatusEnum item : SubStatusEnum.values()) {
				if (item.getStatus().equals(status)) {
					return item;
				}
			}
			return null;
		}

		public static StatusEnum toStatusEnum(SubStatusEnum subStatusEnum) {
			if (subStatusEnum == null) {
				return null;
			}
			if (subStatusEnum == SubStatusEnum.WAIT_PAY) {
				return StatusEnum.WAIT_PAY;
			}
			if (subStatusEnum == SubStatusEnum.CANCELED) {
				return StatusEnum.CANCELED;
			}
			if (subStatusEnum == SubStatusEnum.PAYED) {
				return StatusEnum.WAIT_SEND;
			}
			if (subStatusEnum == SubStatusEnum.WAIT_SEND || subStatusEnum == SubStatusEnum.SENDING
					|| subStatusEnum == SubStatusEnum.SEND_FAIL) {
				return StatusEnum.WAIT_SEND;
			}
			if (subStatusEnum == SubStatusEnum.SEND_SUCCESS) {
				return StatusEnum.WAIT_RECEIVE;
			}
			if (subStatusEnum == SubStatusEnum.WAIT_REVIEW || subStatusEnum == SubStatusEnum.COMPLETE) {
				return StatusEnum.COMPLETE;
			}
			if (subStatusEnum == SubStatusEnum.CHECK || subStatusEnum == SubStatusEnum.REFUNDING
					|| subStatusEnum == SubStatusEnum.REFUND_SUCCESS || subStatusEnum == SubStatusEnum.REFUND_FAIL) {
				return StatusEnum.REFUND;
			}
			return null;
		}
	}

}
