package com.company.order.api.enums;

public interface PayRefundApplyEnum {
	enum BusinessType {
		// 业务类型(USER:用户申请,SYS_AUTO:系统自动退款,AMDIN:管理后台申请)
		USER("USER", "用户申请"), SYS_AUTO("SYS_AUTO", "系统自动退款"), AMDIN("AMDIN", "管理后台申请");
		private String code;
		private String desc;

		BusinessType(String code, String desc) {
			this.code = code;
			this.desc = desc;
		}

		public String getCode() {
			return code;
		}

		public String getDesc() {
			return desc;
		}

		public static BusinessType of(String code) {
			for (BusinessType item : BusinessType.values()) {
				if (item.getCode().equals(code)) {
					return item;
				}
			}
			return null;
		}
		
		public static BusinessType ofRefundTypeEnum(RefundTypeEnum refundType) {
			if (refundType == null) {
				return BusinessType.USER;
			}
			if (refundType == RefundTypeEnum.USER) {
				return BusinessType.USER;
			}
			if (refundType == RefundTypeEnum.SYSTEM_PURCHASE_FAIL || refundType == RefundTypeEnum.SYSTEM_AUTO_REFUND) {
				return BusinessType.SYS_AUTO;
			}
			if (refundType == RefundTypeEnum.ADMIN) {
				return BusinessType.AMDIN;
			}
			return USER;
		}
	}

	enum VerifyStatus {
		WAIT_VERIFY(1, "待审核"), PASS(21, "通过"), REJECT(22, "驳回");

		private Integer code;
		private String desc;

		VerifyStatus(Integer code, String desc) {
			this.code = code;
			this.desc = desc;
		}

		public Integer getCode() {
			return code;
		}

		public String getDesc() {
			return desc;
		}

		public static VerifyStatus of(Integer code) {
			for (VerifyStatus item : VerifyStatus.values()) {
				if (item.getCode().equals(code)) {
					return item;
				}
			}
			return null;
		}
	}

	enum RefundStatus {
		// 1:未退款,2:处理中,22:退款驳回(END),31:已发MQ,31:MQ接收,41:申请成功,42:申请失败(END),51:退款成功(END),52:退款失败(END)
		/* 待处理 */
		NO_REFUND(1, "未退款"),
		/* 驳回 */
		REJECT(21, "退款驳回"),
		/* 通过 */
		DEALING(31, "处理中"), APPLY_SCUESS(41, "申请成功"), APPLY_FAIL(42, "申请失败"), REFUND_SCUESS(51,
				"退款成功"), REFUND_FAIL(52, "退款失败");
		private Integer code;
		private String desc;

		RefundStatus(Integer code, String desc) {
			this.code = code;
			this.desc = desc;
		}

		public Integer getCode() {
			return code;
		}

		public String getDesc() {
			return desc;
		}

		public static RefundStatus of(Integer code) {
			for (RefundStatus item : RefundStatus.values()) {
				if (item.getCode().equals(code)) {
					return item;
				}
			}
			return null;
		}
	}

}
