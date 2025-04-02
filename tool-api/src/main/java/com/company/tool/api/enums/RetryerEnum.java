package com.company.tool.api.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

public interface RetryerEnum {

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
