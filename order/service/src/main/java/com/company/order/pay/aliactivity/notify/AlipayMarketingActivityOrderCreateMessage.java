package com.company.order.pay.aliactivity.notify;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.company.framework.util.JsonUtil;
import com.company.order.mapper.AliActivityNotifyMapper;
import com.company.order.pay.aliactivity.dto.OrderCreateBizContent;

import lombok.extern.slf4j.Slf4j;
/**
 * <pre>
 * 订单已经发放完成，用户已经领取(4、支付宝向用户发券成功，添加至用户卡包,服务商处理用户领券通)
 * 官方文档：https://opendocs.alipay.com/pre-open/02cs7y
 * </pre>
 */
@Slf4j
@Component(FromMessageBeanFactory.ORDERMESSAGE_RECEIVED)
public class AlipayMarketingActivityOrderCreateMessage implements FromMessage {

	@Autowired
	private AliActivityNotifyMapper aliActivityNotifyMapper;

	@Override
	public void handle(Integer payNotifyId, Map<String, String> aliParams) {
		/**
		 * <pre>
		{
			"notify_id": "2020122800222204607000921452504952",
			"utc_timestamp": "1514210452731",
			"msg_method": "alipay.marketing.activity.ordermessage.received",
			"app_id": "3021451196634505",
			"msg_type": "sys",
			"msg_uid": "2088102165945162",
			"biz_content": "{\"id\":\"ORDERSEND_2021042400826001508407723739\",\"order_no\":\"2014081801502300000000140007771420\",\"out_order_no\":\"20123214235435\",\"event_time\":\"12342425435232423\",\"send_status\":\"SUCCESS\"}",
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
			"order_no": "2014081801502300000000140007771420",
			"out_order_no": "20123214235435",
			"event_time": "12342425435232423",
			"send_status": "SUCCESS"
		}
		 * </pre>
		 */
		OrderCreateBizContent bizContentObj = JsonUtil.toEntity(bizContent, OrderCreateBizContent.class);
		log.info("bizContentObj:{}", JsonUtil.toJsonString(bizContentObj));

		// TODO

		// 暂无处理逻辑
		aliActivityNotifyMapper.updateRemarkById("暂无处理逻辑", payNotifyId);
	}

}
