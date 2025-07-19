package com.company.system.api.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

public interface SystemEnum {
	
	@AllArgsConstructor
	enum Type {
		TO_MAIN(1, "外卖-总钱包"), 
		TO_CHARGE(11, "外卖-充值钱包"),
		TO_GIFT(12, "外卖-赠送钱包"),
		
		INVITE_REWARD(2, "邀请奖励"),
		;
		
		@Getter
		private Integer code;
		@Getter
		private String desc;
		
		public static Type of(Integer code) {
			for (Type item : Type.values()) {
				if (item.getCode().equals(code)) {
					return item;
				}
			}
			return null;
		}
	}
	
	@AllArgsConstructor
	enum Status {
		NOMAL(1, "正常"), 
		IN_RISK(2, "风控中"),
		FORZEN(3, "冻结"),
		;

		@Getter
		private Integer code;
		@Getter
		private String desc;
	}
}
