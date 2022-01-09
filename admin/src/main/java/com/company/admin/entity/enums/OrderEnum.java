package com.company.admin.entity.enums;

public interface OrderEnum {

	enum Type {
		RIGHTS("1", "权益"), BUY_MEMBER("2", "购买会员");
		private String code;
		private String desc;

		Type(String code, String desc) {
			this.code = code;
			this.desc = desc;
		}

		public String getCode() {
			return code;
		}

		public String getDesc() {
			return desc;
		}

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