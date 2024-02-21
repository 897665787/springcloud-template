package com.company.order.api.constant;

/**
 * 常量
 */
public interface Constants {
	/**
	 * 服务名
	 */
	String FEIGNCLIENT_VALUE = "template-order";

	public interface BrandRoute {
		String NAIXUE = "naixue";// distribute
		String XICHA = "xicha";// distribute
		String WRITEOFF = "writeoff";// writeoff
		String GROUP_MEAL = "groupmeal";// groupmeal
		String ENT_GROUP_MEAL = "entgroupmeal"; //entgroupmeal
	}

	public interface Deliver {
		String SFTC = "sftc";
		String UUPT = "uupt";
	}

	public interface Order {
		interface BusinessType {
			String BUY_MEMBER = "buy_member";
			String DISTRIBUTE = "distribute";
			String WRITEOFF = "writeoff";
			String GROUP_MEAL = "groupmeal";
			String ENT_GROUP_MEAL = "entgroupmeal";
			String GROUP_MEAL_COUPON = "groupmealcoupon";
			String GROUP_MEAL_HELP_BUY = "groupmealhelpbuy";
		}
	}
}
