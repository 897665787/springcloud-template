package com.company.order.pay.aliactivity.notify;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.company.common.util.JsonUtil;
import com.company.order.entity.AliActivityCoupon;
import com.company.order.mapper.AliActivityCouponMapper;
import com.company.order.mapper.AliActivityNotifyMapper;
import com.company.order.pay.aliactivity.dto.CouponReceivedBizContent;

/**
 * <pre>
 * 券领取通知
 * 官方文档：https://opendocs.alipay.com/pre-open/05hyow
 * </pre>
 */
@Component(FromMessageBeanFactory.MESSAGE_RECEIVED)
public class AlipayMarketingActivityCouponReceivedMessage implements FromMessage {

	@Autowired
	private AliActivityCouponMapper aliActivityCouponMapper;
	@Autowired
	private AliActivityNotifyMapper aliActivityNotifyMapper;

	@Override
	public void handle(Integer payNotifyId, Map<String, String> aliParams) {
		/**
		 * <pre>
		{
			"notify_id": "2020122800222204607000921452504952",
			"utc_timestamp": "1514210452731",
			"msg_method": "alipay.marketing.activity.message.received",
			"app_id": "3021451196634505",
			"msg_type": "sys",
			"msg_uid": "2088102165945162",
			"biz_content": "{\"id\":\"ORDERSEND_2021042400826001508407723739\",\"activity_id\":\"2016042700826004508401111111\",\"event_time\":\"12342425435232423\",\"voucher_id\":\"2021072900073002214009F8QHR3\",\"voucher_code\":\"voucher_code\",\"receive_user_id\":\"2088xxxxx\"}",
			"sign": "gGnICAIp4OyAlnqWgESVFGlPPRfg6NDwblVvC7eZAmnJOBHZKb5OodZHfSs96NM2ff+4sKex5GF6czpzJGZ1VZiYehpZHb6bnoZfRFA5KkoNj5mMkSfzdmnjj/Mmzu5aSz/qUwbtOhF9wR4MUaI1W7xURJNKantewlSRnRAmAbweJgMMVBvNYpGsOkl6wzkXW9GEZPXFQb+NTZeuFcKpPkDurOAGU0aX9YpjE1ouaCxNYqTdsV7+FqTJDaQeYdLWquOFJnDuvxNiC5AOC0m3H19ud8DDvgJi2fvGNkYJ+SlRpPL6sz70dL2HiugnfBEgV0EQJaTTLPiU344BtY68Zg==",
			"sign_type": "RSA2",
			"encrypt_type": "AES",
			"charset": "UTF-8",
			"notify_type": "trade_status",
			"notify_time": "2020-12-28 20:46:07",
			"auth_app_id": "3021451196634505"
		}
		 * </pre>
		 */
		String bizContent = aliParams.get("biz_content");

		/**
		 * <pre>
		{
			"id": "ORDERSEND_2021042400826001508407723739",
			"activity_id": "2016042700826004508401111111",
			"event_time": "12342425435232423",
			"voucher_id": "2021072900073002214009F8QHR3",
			"voucher_code": "voucher_code",
			"receive_user_id": "2088xxxxx"
		}
		 * </pre>
		 */
		CouponReceivedBizContent bizContentObj = JsonUtil.toEntity(bizContent, CouponReceivedBizContent.class);

		// 回调数据落库
		AliActivityCoupon aliActivityCouponDB = aliActivityCouponMapper.selectByEventId(bizContentObj.getId());
		if (aliActivityCouponDB != null) {
			// 订单回调已处理完成，无需重复处理
			aliActivityNotifyMapper.updateRemarkById("回调已处理完成，无需重复处理", payNotifyId);
			return;
		}

		AliActivityCoupon aliActivityCoupon = new AliActivityCoupon();
		aliActivityCoupon.setActivityId(bizContentObj.getActivityId());
		aliActivityCoupon.setEventId(bizContentObj.getId());
		aliActivityCoupon.setEventTime(bizContentObj.getEventTime());
		aliActivityCoupon.setVoucherId(bizContentObj.getVoucherId());
		aliActivityCoupon.setVoucherCode(bizContentObj.getVoucherCode());
		aliActivityCoupon.setReceiveUserId(bizContentObj.getReceiveUserId());
		aliActivityCoupon.setPayNotifyId(payNotifyId);

		aliActivityCouponMapper.insert(aliActivityCoupon);
	}

}
