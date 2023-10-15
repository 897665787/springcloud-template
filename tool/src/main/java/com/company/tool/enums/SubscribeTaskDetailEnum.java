package com.company.tool.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

public interface SubscribeTaskDetailEnum {

	@AllArgsConstructor
	enum Status {
		// 1:待发送,2:待定时发送,11:发送MQ成功,12:MQ消费成功,21:请求成功,22:请求失败,23:发送成功,24:发送失败,31:取消发送
		/* 过渡态 */
		PRE_SEND(1, "待发送"), PRE_TIME_SEND(2, "待定时发送"), SEND_MQ_SUCCESS(11, "发送MQ成功"), CONS_MQ_SUCCESS(12, "MQ消费成功")
		/* 终态 */
		, REQ_SUCCESS(21, "请求成功"), REQ_FAIL(22, "请求失败")
		, SEND_CANCEL(31, "取消发送");

		@Getter
		private Integer code;
		@Getter
		private String desc;

		public static Status of(Integer code) {
			for (Status item : Status.values()) {
				if (item.getCode().equals(code)) {
					return item;
				}
			}
			return null;
		}
	}
}
