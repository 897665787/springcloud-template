package com.company.tool.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

public interface RetryTaskEnum {
	
	@AllArgsConstructor
	enum Status {
		/* 正常 */
		PRE_CALL(1, "待调用"), CALL_SUCCESS(2, "调用成功(END)"),
		/* 异常 */
		CALL_FAIL(21, "调用失败"), ABANDON_CALL(22, "放弃调用(END)");

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
