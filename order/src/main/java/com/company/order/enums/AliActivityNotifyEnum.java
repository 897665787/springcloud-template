package com.company.order.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

public interface AliActivityNotifyEnum {

	@AllArgsConstructor
	enum Method {
		SPIORDERSEND("spiordersend", "订单券发放"), FROM("from", "FROM消息");
		@Getter
		private String code;
		@Getter
		private String desc;

		public static Method of(String code) {
			for (Method item : Method.values()) {
				if (item.getCode().equals(code)) {
					return item;
				}
			}
			return null;
		}
	}
}
