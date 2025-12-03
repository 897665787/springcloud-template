package com.company.order.pay.aliactivity.notify;

import java.math.BigDecimal;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Component;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.company.framework.util.JsonUtil;
import com.company.framework.messagedriven.MessageSender;
import com.company.framework.messagedriven.constants.FanoutConstants;
import com.company.framework.sequence.SequenceGenerator;
import com.company.order.messagedriven.Constants;
import com.company.order.messagedriven.strategy.StrategyConstants;
import com.company.order.api.enums.OrderPayEnum;
import com.company.order.api.enums.OrderPayRefundEnum;
import com.company.order.api.enums.PayRefundApplyEnum;
import com.company.order.entity.AliActivityPay;
import com.company.order.entity.AliActivityPayRefund;
import com.company.order.entity.OrderPay;
import com.company.order.entity.OrderPayRefund;
import com.company.order.entity.PayRefundApply;
import com.company.order.mapper.AliActivityNotifyMapper;
import com.company.order.mapper.AliActivityPayMapper;
import com.company.order.mapper.AliActivityPayRefundMapper;
import com.company.order.mapper.PayRefundApplyMapper;
import com.company.order.pay.aliactivity.AliActivityConstants;
import com.company.order.pay.aliactivity.dto.OrderRefundBizContent;
import com.company.order.service.FinancialFlowService;
import com.company.order.service.OrderPayRefundService;
import com.company.order.service.OrderPayService;
import com.google.common.collect.Maps;

import lombok.extern.slf4j.Slf4j;

/**
 * <pre>
 * 退款回调通知(3、支付宝向用户发券失败，支付宝自动发起退款，服务商处理退款回调通知)
 * 官方文档：https://opendocs.alipay.com/pre-open/02cvz6
 * </pre>
 */
@Slf4j
@Component(FromMessageBeanFactory.ORDERMESSAGE_REFUNDED)
public class AlipayMarketingActivityOrderRefundMessage implements FromMessage {

	@Autowired
	private AliActivityPayMapper aliActivityPayMapper;
	@Autowired
	private AliActivityPayRefundMapper aliActivityPayRefundMapper;

	@Autowired
	private AliActivityNotifyMapper aliActivityNotifyMapper;

	@Autowired
	private MessageSender messageSender;

	@Autowired
	private SequenceGenerator sequenceGenerator;
	@Autowired
	private PayRefundApplyMapper payRefundApplyMapper;
	@Autowired
	private OrderPayService orderPayService;
	@Autowired
	private OrderPayRefundService orderPayRefundService;
	@Autowired
	private FinancialFlowService financialFlowService;

	@Override
	public void handle(Integer payNotifyId, Map<String, String> aliParams) {
		/**
		 * <pre>
		{
			"notify_id": "2020122800222204607000921452504952",
			"utc_timestamp": "1514210452731",
			"msg_method": "alipay.marketing.activity.ordermessage.refunded",
			"app_id": "3021451196634505",
			"msg_type": "sys",
			"msg_uid": "2088102165945162",
			"biz_content": "{\"id\":\"ORDERSEND_2021042400826001508407723739\",\"order_no\":\"2014081801502300000000140007771420\",\"out_order_no\":\"20123214235435\",\"event_time\":\"12342425435232423\",\"refund_type\":\"USER_REFUND\",\"send_status\":\"SUCCESS\",\"out_biz_no\":\"12342sdfsdf\"}",
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
		 * 如果是USER_REFUND场景。则幂等id为alipay.marketing.activity.order.refund传入的out_biz_no。其他场景，幂等id为空。
		 * 用户主动退款（USER_REFUND）情况：
		{
			"id": "ORDERSEND_2021042400826001508407723739",
			"order_no": "2014081801502300000000140007771420",
			"out_order_no": "20123214235435",
			"event_time": "1672740605064",
			"refund_type": "USER_REFUND",
			"refund_status": "SUCCESS",
			"out_biz_no": "12342sdfsdf"
		}
			支付宝主动退款（SEND_ERROR_REFUND）情况：
		{
			"order_no": "2023010301502300000009120012095700",
			"refund_status": "SUCCESS",
			"refund_type": "SEND_ERROR_REFUND",
			"out_order_no": "472224544699228160",
			"id": "ORDERREFUND_2023010301502300000009120012095700",
			"event_time": "1672740605064"
		}
		 * </pre>
		 */
		OrderRefundBizContent bizContentObj = JsonUtil.toEntity(bizContent, OrderRefundBizContent.class);

		String outOrderNo = bizContentObj.getOutOrderNo();
		String orderNo = bizContentObj.getOrderNo();
		String outBizNo = bizContentObj.getOutBizNo();
		String refundType = bizContentObj.getRefundType();

		// 回调数据落库
		AliActivityPay aliActivityPay = aliActivityPayMapper.selectByOutOrderNo(outOrderNo);

		AliActivityPayRefund aliActivityPayRefund = aliActivityPayRefundMapper.selectByOutOrderNo(outOrderNo);
		if (aliActivityPayRefund == null) {// 查不到数据，说明是‘支付宝主动退款’
			// -> 模拟用户申请退款操作，补全退款流程相关的表数据
			if (StringUtils.isBlank(outBizNo)) {
				outBizNo = String.valueOf(sequenceGenerator.nextId());
			}

			AliActivityPayRefund aliActivityPay4Insert = new AliActivityPayRefund()
					.setAppid(aliActivityPay.getAppid())
					.setOutOrderNo(outOrderNo)
					.setOutBizNo(outBizNo)
					.setBuyerId(aliActivityPay.getBuyerId())
//					.setRefundActivityInfoList()// 支付宝主动退款不需要填写
					.setRefundStatus(AliActivityConstants.RefundStatus.REFUND_SUCCESS)
					.setOrderNo(orderNo)
					.setRefundType(refundType)
			;
			try {
				aliActivityPayRefundMapper.insert(aliActivityPay4Insert);
			} catch (DuplicateKeyException e) {
				// 订单回调已处理完成，无需重复处理
				aliActivityNotifyMapper.updateRemarkById("订单回调已处理完成，无需重复处理", payNotifyId);
				return;
			}

			// 创建退款申请（补数据）
			PayRefundApply payRefundApply = new PayRefundApply();
			payRefundApply.setOrderCode(outBizNo);
			payRefundApply.setOldOrderCode(outOrderNo);
			payRefundApply.setAmount(aliActivityPay.getTotalAmount());
			payRefundApply.setBusinessType(PayRefundApplyEnum.BusinessType.SYS_AUTO.getCode());
			payRefundApply.setVerifyStatus(PayRefundApplyEnum.VerifyStatus.PASS.getCode());
			payRefundApply.setRefundStatus(PayRefundApplyEnum.RefundStatus.REFUND_SUCCESS.getCode());
			payRefundApply.setReason("支付宝主动退款");
			payRefundApply.setRemark("支付宝主动退款");
			payRefundApplyMapper.insert(payRefundApply);

			// 创建退款订单（补数据）
			OrderPayRefund refundOrderPay = new OrderPayRefund();
			OrderPay orderPay = orderPayService.selectByOrderCode(outOrderNo);
			refundOrderPay.setUserId(orderPay.getUserId());
			refundOrderPay.setBusinessType(OrderPayRefundEnum.BusinessType.SYS_AUTO.getCode());
			refundOrderPay.setMethod(OrderPayEnum.Method.ALIACTIVITY.getCode());
			refundOrderPay.setOrderCode(outOrderNo);
			refundOrderPay.setRefundOrderCode(outBizNo);
			refundOrderPay.setAmount(aliActivityPay.getTotalAmount());
			refundOrderPay.setRefundAmount(aliActivityPay.getTotalAmount());
			refundOrderPay.setStatus(OrderPayRefundEnum.Status.REFUND_SUCCESS.getCode());
			refundOrderPay.setRemark("支付宝主动退款");
			orderPayRefundService.save(refundOrderPay);

			financialFlow(orderNo, aliActivityPay.getTotalAmount(), outOrderNo, outBizNo);

			Map<String, Object> params = Maps.newHashMap();
			params.put("success", true);
			params.put("message", "支付宝主动退款");
			params.put("orderCode", outOrderNo);
			params.put("refundOrderCode", outBizNo);
			// params.put("attach", attach);
			params.put("thisRefundAmount", aliActivityPay.getTotalAmount());
			params.put("totalRefundAmount", aliActivityPay.getTotalAmount());
			params.put("refundAll", true);

			messageSender.sendFanoutMessage(params, FanoutConstants.REFUND_APPLY_RESULT.EXCHANGE);
		} else {// 用户主动退款
			AliActivityPayRefund aliActivityPay4Update = new AliActivityPayRefund()
					.setOutBizNo(outBizNo)
					.setRefundStatus(AliActivityConstants.RefundStatus.REFUND_SUCCESS)
					.setOrderNo(orderNo)
					.setRefundType(refundType)
					;

			UpdateWrapper<AliActivityPayRefund> wrapper = new UpdateWrapper<AliActivityPayRefund>();
			wrapper.and(i -> i.isNull("refund_status")
					.or(i2 -> i2.ne("refund_status", AliActivityConstants.RefundStatus.REFUND_SUCCESS)));
			int affect = aliActivityPayRefundMapper.update(aliActivityPay4Update, wrapper);
			if (affect == 0) {
				// 订单回调已处理完成，无需重复处理
				aliActivityNotifyMapper.updateRemarkById("订单回调已处理完成，无需重复处理", payNotifyId);
				return;
			}

			// MQ异步处理
			Map<String, Object> params = Maps.newHashMap();
			params.put("outTradeNo", outOrderNo);
			params.put("outRefundNo", outBizNo);
			params.put("success", true);

			//财务流水信息
			params.put("amount", aliActivityPay.getTotalAmount());
			params.put("orderPayMethod", OrderPayEnum.Method.ALIACTIVITY.getCode());
			params.put("merchantNo", "未配置");
			params.put("tradeNo", aliActivityPay.getTradeNo());

			messageSender.sendNormalMessage(StrategyConstants.REFUND_NOTIFY_STRATEGY, params, "${messagedriven.exchange.direct}",
					"${messagedriven.queue.common.key}");
		}
	}

	private void financialFlow(String orderNo, BigDecimal amount, String outTradeNo, String outRefundNo) {
		//数据落库, 生成财务流水(出账)
		try {
			String merchantNo =  "未配置";
			financialFlowService.outAccount(outTradeNo, outRefundNo, orderNo, amount, OrderPayEnum.Method.ALIACTIVITY, merchantNo);
		} catch (Exception e) {
			log.error("生成财务流水(出账), error: {}", e);
		}
	}

}
