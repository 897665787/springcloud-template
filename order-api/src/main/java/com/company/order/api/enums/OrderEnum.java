package com.company.order.api.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

public interface OrderEnum {
	// 订单类型order_type
	@AllArgsConstructor
	enum OrderType {
		@Deprecated
		UNKNOWN("unknown", "未知"), // 不能在别的地方用
		
		BUY_MEMBER("buy_member", "购买会员"), // 开会员订单
		DISTRIBUTE("distribute", "配送"), // 含运力配送订单
		WRITEOFF("writeoff", "核销码"), // 需核销订单
		MOVIE("movie", "电影票"),// 电影票订单
		SUBORDERDEMO3("subOrderDemo3", "子订单demo3"),// 子订单demo3
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
			// 替换UNKNOWN的code返回，可以在getCode时获取到没有写到枚举中的code(注意：这里会持久化改掉UNKNOWN的code)
			OrderType unknown = UNKNOWN;
			unknown.code = code;
			return unknown;
		}
	}

	// 主状态status
	@AllArgsConstructor
	enum StatusEnum {
		WAIT_PAY(1, "待付款"), // 待支付
		CANCELED(2, "已取消"), // 已取消 END
		WAIT_SEND(3, "待发货"), // (已付款、待发货、发货中、发货失败)
		WAIT_RECEIVE(4, "待收货"), // (发货成功)
		COMPLETE(5, "已完成"), // (待评价、已结束)
		REFUND(6, "退款/售后"),// (待审核、退款中、退款成功)
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
		WAIT_PAY(11, "待付款"), // 待支付
		CANCELED(21, "已取消"), // 已取消 END
		
		PAYED(30, "已付款"), //
		WAIT_SEND(31, "待发货"), //
		SENDING(32, "发货中"), //
		SEND_FAIL(33, "发货失败"), //
		
		SEND_SUCCESS(41, "已发货"), // 一段时间内没有收货则自动变为已收货（job sendSuccess2ReceiveHandler）
		
		WAIT_REVIEW(51, "待评价"), // 一段时间内没有评价则自动变为已结束（job waitReview2completeHandler）
		COMPLETE(52, "已结束"), // END
		
		CHECK(60, "退款审核中"), // 待审核
		REFUNDING(61, "退款中"), //
		REFUND_SUCCESS(62, "退款成功"), // END
//		REFUND_FAIL(63, "退款失败"),// 没有该状态，失败的情况下还原状态，退款失败这个行为可以记录到remark字段
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
			if (subStatusEnum == SubStatusEnum.PAYED || subStatusEnum == SubStatusEnum.WAIT_SEND
					|| subStatusEnum == SubStatusEnum.SENDING || subStatusEnum == SubStatusEnum.SEND_FAIL) {
				return StatusEnum.WAIT_SEND;
			}
			if (subStatusEnum == SubStatusEnum.SEND_SUCCESS) {
				return StatusEnum.WAIT_RECEIVE;
			}
			if (subStatusEnum == SubStatusEnum.WAIT_REVIEW || subStatusEnum == SubStatusEnum.COMPLETE) {
				return StatusEnum.COMPLETE;
			}
			if (subStatusEnum == SubStatusEnum.CHECK || subStatusEnum == SubStatusEnum.REFUNDING
					|| subStatusEnum == SubStatusEnum.REFUND_SUCCESS) {
				return StatusEnum.REFUND;
			}
			return null;
		}
	}

	@AllArgsConstructor
	enum SubOrderEventEnum {
		USER_CANCEL("user_cancel", "用户主动取消订单"), //
		QUERY_ITEM("query_item", "查询订单列表子项"), //
		QUERY_DETAIL("query_detail", "查询订单详情"),//
		CALC_CANREFUNDAMOUNT("canRefundAmount", "计算可退款金额"),// 可不实现，不实现的情况下使用订单默认逻辑，该类型固定返回BigDecimal类型
		;

		@Getter
		private String code;
		@Getter
		private String desc;
	}

}
