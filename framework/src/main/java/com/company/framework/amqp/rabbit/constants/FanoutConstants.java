package com.company.framework.amqp.rabbit.constants;

/**
 * Fanout广播事件一般基于交换机来做
 */
public interface FanoutConstants {

	String PREFIX = "fanout-";

	// 部署事件
	interface DEPLOY {
		// 交换机
		String EXCHANGE = PREFIX + "deploy";
	}

	// canal事件
	interface CANAL {
		// 交换机
		String EXCHANGE = FanoutConstants.PREFIX + "canal";

		String PREFIX = FanoutConstants.PREFIX + "canal-";
		// 队列
		String USER_QUEUE = PREFIX + "user";
		String ORDER_QUEUE = PREFIX + "order";
	}
	
	// 订单创建事件
	interface ORDER_CREATE {
		// 交换机
		String EXCHANGE = FanoutConstants.PREFIX + "order_create";

		String PREFIX = FanoutConstants.PREFIX + "order_create-";
		// 队列
		String SMS_QUEUE = PREFIX + "sms";
		String COUNTMONEY_QUEUE = PREFIX + "countmoney";
	}

	// 用户来源事件
	interface USER_SOURCE {
		// 交换机
		String EXCHANGE = FanoutConstants.PREFIX + "user_source";

		String PREFIX = FanoutConstants.PREFIX + "user_source-";
		// 队列
		String SOURCE_RECORD_QUEUE = PREFIX + "source_record";
	}
	
	// 用户登录事件
	interface USER_LOGIN {
		// 交换机
		String EXCHANGE = FanoutConstants.PREFIX + "user_login";

		String PREFIX = FanoutConstants.PREFIX + "user_login-";
		// 队列
		String LOGIN_RECORD_QUEUE = PREFIX + "login_record";
	}

	// 用户注册事件
	interface USER_REGISTER {
		// 交换机
		String EXCHANGE = FanoutConstants.PREFIX + "user_register";

		String PREFIX = FanoutConstants.PREFIX + "user_register-";
		// 队列
		String BIND_EMAIL_QUEUE = PREFIX + "bind_email";
		String BIND_MOBILE_QUEUE = PREFIX + "bind_mobile";
	}

	// 用户登出事件
	interface USER_LOGOUT {
		// 交换机
		String EXCHANGE = FanoutConstants.PREFIX + "user_logout";

		String PREFIX = FanoutConstants.PREFIX + "user_logout-";
		// 队列
		String LOGOUT_RECORD_QUEUE = PREFIX + "logout_record";
	}
	
	// 优惠券发放事件
	interface SEND_COUPON {
		// 交换机
		String EXCHANGE = FanoutConstants.PREFIX + "send_coupon";
		
		String PREFIX = FanoutConstants.PREFIX + "send_coupon-";
		// 队列
		String SUBSCRIBE_RECEIVE_QUEUE = PREFIX + "subscribe_receive";
		String SUBSCRIBE_TOUSE_QUEUE = PREFIX + "subscribe_touse";
		String SUBSCRIBE_EXPIRE_QUEUE = PREFIX + "subscribe_expire";
	}
	
	// 退款申请结果事件
	interface REFUND_APPLY_RESULT {
		// 交换机
		String EXCHANGE = FanoutConstants.PREFIX + "refund_apply_result";
		
		String PREFIX = FanoutConstants.PREFIX + "refund_apply_result-";
		// 队列
		String MEMBER_BUY_QUEUE = PREFIX + "member_buy";
		String GOODS_REFUND_QUEUE = PREFIX + "goods_refund";
		String RECHARGE_REFUND_QUEUE = PREFIX + "recharge";
	}
	
	// 购买会员-支付成功事件
	interface MEMBER_BUY_PAY_SUCCESS {
		// 交换机
		String EXCHANGE = FanoutConstants.PREFIX + "member_buy_pay_success";
		
		String PREFIX = FanoutConstants.PREFIX + "member_buy_pay_success-";
		// 队列
//		String GOODS_REFUND_QUEUE = PREFIX + "goods_refund";
	}
	
	// 配送订单-支付成功事件
	interface DISTRIBUTE_PAY_SUCCESS {
		// 交换机
		String EXCHANGE = FanoutConstants.PREFIX + "distribute_pay_success";
		
		String PREFIX = FanoutConstants.PREFIX + "distribute_pay_success-";
		// 队列
//		String GOODS_REFUND_QUEUE = PREFIX + "goods_refund";
	}
	
	// 子订单demo3-支付成功事件
	interface SUBORDERDEMO3_PAY_SUCCESS {
		// 交换机
		String EXCHANGE = FanoutConstants.PREFIX + "subOrderDemo3_pay_success";
		
		String PREFIX = FanoutConstants.PREFIX + "subOrderDemo3_pay_success-";
		// 队列
//		String GOODS_REFUND_QUEUE = PREFIX + "goods_refund";
	}
	
	// 充值-支付成功事件
	interface RECHARGE_PAY_SUCCESS {
		// 交换机
		String EXCHANGE = FanoutConstants.PREFIX + "recharge_pay_success";
		
		String PREFIX = FanoutConstants.PREFIX + "recharge_pay_success-";
		// 队列
//		String GOODS_REFUND_QUEUE = PREFIX + "goods_refund";
	}
	
	// admin用户登录事件
	interface SYS_USER_LOGIN {
		// 交换机
		String EXCHANGE = FanoutConstants.PREFIX + "sys_user_login";

		String PREFIX = FanoutConstants.PREFIX + "sys_user_login-";
		// 队列
		String SYS_LOGIN_RECORD_QUEUE = PREFIX + "sys_login_record";
		String INCR_EXPIRELOGINTIMES_QUEUE = PREFIX + "incr_expirelogintimes";
	}
}
