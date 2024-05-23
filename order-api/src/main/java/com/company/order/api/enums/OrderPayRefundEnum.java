package com.company.order.api.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

public interface OrderPayRefundEnum {

	@AllArgsConstructor
	enum BusinessType {
		USER("user", "用户申请"), SYS_AUTO("sys_auto", "系统自动退款"), AMDIN("amdin", "管理后台申请");
		@Getter
		private String code;
		@Getter
		private String desc;

		public static BusinessType of(String code) {
			for (BusinessType item : BusinessType.values()) {
				if (item.getCode().equals(code)) {
					return item;
				}
			}
			return null;
		}
	}

	@AllArgsConstructor
	enum Status {
		WAIT_APPLY("wait_apply", "待申请"), //
		REFUND_SUCCESS("refund_success", "退款成功"), //
		REFUND_FAIL("refund_fail", "退款失败");
		@Getter
		private String code;
		@Getter
		private String desc;

		public static Status of(String code) {
			for (Status item : Status.values()) {
				if (item.getCode().equals(code)) {
					return item;
				}
			}
			return null;
		}
	}

}
