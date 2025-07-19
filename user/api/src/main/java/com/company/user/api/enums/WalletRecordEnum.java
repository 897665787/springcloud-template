package com.company.user.api.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

public interface WalletRecordEnum {
	
	@AllArgsConstructor
	enum Type {
		IN(1, "入账"), OUT(2, "出账");

		@Getter
		private Integer code;
		@Getter
		private String desc;
	}

	@AllArgsConstructor
	enum IncomeRecordUseStatus {
		UN_USED(1, "未使用"), SUB_USED(2, "部分使用"), ALL_USED(3, "全部使用"), EXPIRED(4, "已过期");

		@Getter
		private Integer code;
		@Getter
		private String message;

		public static String of(Integer code) {
			for (IncomeRecordUseStatus status : IncomeRecordUseStatus.values()) {
				if (code.equals(status.getCode())) {
					return status.getMessage();
				}
			}
			return null;
		}

	}
}
