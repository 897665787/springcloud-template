package com.company.order.api.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

public interface OrderPayEnum {

	@AllArgsConstructor
	enum BusinessType {
		@Deprecated
		UNKNOWN("unknown", "未知"), // 不能在别的地方用
		
		NOMAL("nomal", "普通下单"), //
		KILL("kill", "秒杀下单"), //
		MEMBER("member", "购买会员"), //
		DISTRIBUTE("distribute", "配送订单"), //
		SUBORDERDEMO3("subOrderDemo3", "子订单demo3"), //
		;
		@Getter
		private String code;
		@Getter
		private String desc;

		public static BusinessType of(String code) {
			for (BusinessType item : BusinessType.values()) {
				if (item.getCode().equals(code)) {
					return item;
				}
			}
			// 替换UNKNOWN的code返回，可以在getCode时获取到没有写到枚举中的code(注意：这里会持久化改掉UNKNOWN的code)
			BusinessType unknown = UNKNOWN;
			unknown.code = code;
			return unknown;
		}
	}

	@AllArgsConstructor
	enum Method {
		ALI("ali", "支付宝"), //
		ALIACTIVITY("aliactivity", "支付宝活动"), //
		WX("wx", "微信"), //
		IOS("ios", "苹果"), //
		QUICK("quick", "云闪付")//
		;
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

	@AllArgsConstructor
	enum TradeType {
		APP("APP", "App支付"), //
		JSAPI("JSAPI", "公众号支付/小程序支付"), //
		MWEB("MWEB", "H5支付")//
		;
		@Getter
		private String code;
		@Getter
		private String desc;

		public static TradeType of(String code) {
			for (TradeType item : TradeType.values()) {
				if (item.getCode().equals(code)) {
					return item;
				}
			}
			return null;
		}
	}

	@AllArgsConstructor
	enum Status {
		WAITPAY("waitpay", "待支付"), //
		CLOSED("closed", "已关闭"), //
		PAYED("payed", "已支付")//
		;
		@Getter
		private String code;
		@Getter
		private String desc;

		public static Status of(String code) {
			for (Status item : Status.values()) {
				if (item.getCode().equals(code)) {
					return item;
				}
			}
			return null;
		}
	}

}
