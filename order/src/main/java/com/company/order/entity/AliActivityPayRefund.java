package com.company.order.entity;

import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;

import lombok.Data;
import lombok.experimental.Accessors;

@TableName("bu_ali_activity_pay_refund")
@Data
@Accessors(chain = true)
public class AliActivityPayRefund {
	/**
	 * ID
	 */
	@TableId(type = IdType.AUTO)
	private Integer id;

	/**
	 * 申请商户号的appid或商户号绑定的appid
	 */
	private String appid;

	/**
	 * 商户订单号
	 */
	private String outOrderNo;

	/**
	 * 外部业务单号，用作幂等控制。
	 * 
	 * 注：针对同一次退款请求，如果调用接口失败或异常了，重试时需要保证退款请求号不能变更，防止该笔交易重复退款。
	 * 支付宝会保证同样的退款请求号多次请求只会退一次。
	 * 
	 * 该外部业务单号会在后续alipay.marketing.activity.message.refund 退款消息中带上。
	 */
	private String outBizNo;
	
	/**
	 * 购买者的支付宝uid
	 */
	private String buyerId;
	
	/**
	 * 退款活动信息列表
	 * (activity_id,quantity,voucher_code_list)
	 */
	private String refundActivityInfoList;
	
	/**
	 * 交易结果(TRADE_CLOSED:交易关闭,TRADE_FINISHED:交易完结,TRADE_SUCCESS:支付成功,WAIT_BUYER_PAY:交易创建)
	 */
	private String tradeStatus;

	/**
	 * 购买商家兑换券的营销订单号
	 */
	private String orderNo;
	
	/**
	 * <pre>
	 * 退款场景。 
	 * USER_REFUND:用户主动发起退款的场景。通过退款接口调用成功的，均认为是用户主动发起退款。
	 * AUTO_EXPIRE:平台过期自动退款的场景。
	 * SEND_ERROR_REFUND:由于多次调用服务商实现的spi接口：spi.alipay.marketing.activity.order.send失败，平台发起自动退款。
	 * ALIPAY_EMERGENCY_REFUND:支付宝应急退款。（该场景一定是与服务商协商后才会执行）
	 * </pre>
	 */
	private String refundType;
	
	/**
	 * 关联bu_pay_notify ID
	 */
	private Integer payNotifyId;
	
	/**
	 * 备注信息
	 */
	private String remark;

	/**
	 * 创建时间
	 */
	private LocalDateTime createTime;

	/**
	 * 更新时间
	 */
	private LocalDateTime updateTime;
}