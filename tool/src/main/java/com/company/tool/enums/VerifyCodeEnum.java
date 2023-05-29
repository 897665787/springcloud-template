package com.company.tool.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

public interface VerifyCodeEnum {
	
	@AllArgsConstructor
	enum Status {
		UN_USE(1, "未使用"), USED(2, "已使用");

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
