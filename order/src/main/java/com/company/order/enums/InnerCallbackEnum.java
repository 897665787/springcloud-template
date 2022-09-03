package com.company.order.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

public interface InnerCallbackEnum {
	
	@AllArgsConstructor
	enum Status {
		/* 正常 */
		PRE_CALLBACK(1, "待回调"), CALLBACK_SUCCESS(2, "回调成功(END)"),
		/* 异常 */
		CALLBACK_FAIL(21, "回调失败"), ABANDON_CALLBACK(22, "放弃回调(END)");

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

	String SECONDSSTRATEGY_INCREMENT = "increment";
	String SECONDSSTRATEGY_FIX = "fix";
	
	@AllArgsConstructor
	enum SecondsStrategy {
		INCREMENT(SECONDSSTRATEGY_INCREMENT, "增量"), FIX(SECONDSSTRATEGY_FIX, "固定");

		@Getter
		private String code;
		@Getter
		private String desc;

		public static SecondsStrategy of(String code) {
			for (SecondsStrategy item : SecondsStrategy.values()) {
				if (item.getCode().equals(code)) {
					return item;
				}
			}
			return null;
		}
	}
}
