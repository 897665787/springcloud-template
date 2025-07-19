package com.company.tool.api.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

public interface SubscribeEnum {
	
	interface GROUP {
		String NEW_USER_COUPON_PKG = "new_user_coupon_pkg";// 新人券包
	}
	
	interface _TYPE {
		String COUPON_REVICE = "coupon_revice";
		String COUPON_USE = "coupon_use";
		String COUPON_EXPIRE = "coupon_expire";
		
		String ACTIVE_START = "active_start";
		String ACTIVE_PROGRESS = "active_progress";
	}

	@AllArgsConstructor
	enum Type {
		COUPON_REVICE(_TYPE.COUPON_REVICE, "优惠券到账提醒"), //
		COUPON_USE(_TYPE.COUPON_USE, "优惠券使用提醒"), //
		COUPON_EXPIRE(_TYPE.COUPON_EXPIRE, "优惠券过期提醒"), //

		ACTIVE_START(_TYPE.ACTIVE_START, "活动开始通知"), //
		ACTIVE_PROGRESS(_TYPE.ACTIVE_PROGRESS, "活动进度通知"), //

		;

		@Getter
		private String code;
		@Getter
		private String desc;

		public static Type of(String code) {
			for (Type item : Type.values()) {
				if (item.getCode().equals(code)) {
					return item;
				}
			}
			return null;
		}
	}
}
