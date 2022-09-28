package com.company.order.controller;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.company.common.api.Result;
import com.company.common.util.JsonUtil;
import com.company.framework.amqp.MessageSender;
import com.company.framework.redis.redisson.DistributeLockUtils;
import com.company.order.api.enums.OrderPayEnum;
import com.company.order.api.enums.OrderPayRefundEnum;
import com.company.order.api.feign.PayFeign;
import com.company.order.api.request.PayCloseReq;
import com.company.order.api.request.PayRefundReq;
import com.company.order.api.request.PayReq;
import com.company.order.api.request.RefundReq;
import com.company.order.api.response.PayInfoResp;
import com.company.order.api.response.PayRefundResp;
import com.company.order.api.response.PayResp;
import com.company.order.api.response.PayTradeStateResp;
import com.company.order.entity.AliPay;
import com.company.order.entity.AliPayRefund;
import com.company.order.entity.OrderPay;
import com.company.order.entity.OrderPayRefund;
import com.company.order.entity.PayNotify;
import com.company.order.entity.WxPay;
import com.company.order.entity.WxPayRefund;
import com.company.order.innercallback.processor.bean.InnerCallbackProcessorBeanName;
import com.company.order.innercallback.processor.bean.ProcessorBeanName;
import com.company.order.innercallback.service.IInnerCallbackService;
import com.company.order.mapper.AliPayMapper;
import com.company.order.mapper.AliPayRefundMapper;
import com.company.order.mapper.PayNotifyMapper;
import com.company.order.mapper.WxPayMapper;
import com.company.order.mapper.WxPayRefundMapper;
import com.company.order.pay.PayFactory;
import com.company.order.pay.ali.AliConstants;
import com.company.order.pay.ali.config.AliPayConfiguration;
import com.company.order.pay.ali.config.AliPayProperties;
import com.company.order.pay.core.PayClient;
import com.company.order.pay.dto.PayParams;
import com.company.order.pay.wx.config.WxPayConfiguration;
import com.company.order.pay.wx.config.WxPayProperties;
import com.company.order.rabbitmq.Constants;
import com.company.order.rabbitmq.consumer.strategy.StrategyConstants;
import com.company.order.service.OrderPayRefundService;
import com.company.order.service.OrderPayService;
import com.github.binarywang.wxpay.bean.notify.WxPayNotifyResponse;
import com.github.binarywang.wxpay.bean.notify.WxPayOrderNotifyResult;
import com.github.binarywang.wxpay.bean.notify.WxPayRefundNotifyResult;
import com.github.binarywang.wxpay.bean.notify.WxPayRefundNotifyResult.ReqInfo;
import com.github.binarywang.wxpay.bean.result.BaseWxPayResult;
import com.github.binarywang.wxpay.constant.WxPayConstants;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.github.binarywang.wxpay.util.SignUtils;
import com.google.common.collect.Maps;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping(value = "/pay")
public class PayController implements PayFeign {

	@Autowired
	private OrderPayService orderPayService;

	@Autowired
	private WxPayMapper wxPayMapper;
	@Autowired
	private WxPayRefundMapper wxPayRefundMapper;

	@Autowired
	private AliPayMapper aliPayMapper;
	@Autowired
	private AliPayRefundMapper aliPayRefundMapper;

	@Autowired
	private PayNotifyMapper payNotifyMapper;

	@Autowired
	private MessageSender messageSender;
	
	@Autowired
	private OrderPayRefundService orderPayRefundService;

	@Autowired
	private IInnerCallbackService innerCallbackService;
	
	private static final String NOTIFY_URL_REFUND = "http://template-order/pay/refundWithRetry";
	
	@Override
	public Result<PayResp> unifiedorder(@RequestBody @Valid PayReq payReq) {

		String orderCode = payReq.getOrderCode();
		String key = String.format("lock:orderpay:ordercode:%s", orderCode);
		PayResp payRespR = DistributeLockUtils
				.doInDistributeLockThrow(key, () -> {
					OrderPay orderPay = orderPayService.selectByOrderCode(orderCode);
					if (orderPay == null) {
						orderPay = new OrderPay();
						orderPay.setUserId(payReq.getUserId());
						orderPay.setOrderCode(orderCode);
						orderPay.setBusinessType(payReq.getBusinessType().getCode());
						orderPay.setMethod(payReq.getMethod().getCode());
						orderPay.setAppid(payReq.getAppid());
						orderPay.setAmount(payReq.getAmount());
						orderPay.setBody(payReq.getBody());
						orderPay.setStatus(OrderPayEnum.Status.WAITPAY.getCode());
						orderPay.setNotifyUrl(payReq.getNotifyUrl());
						orderPay.setNotifyAttach(payReq.getAttach());
						orderPay.setRemark(payReq.getRemark());
						
						orderPayService.insert(orderPay);
					} else {
						OrderPay orderPay4Update = new OrderPay();
						orderPay4Update.setId(orderPay.getId());
						orderPay4Update.setBusinessType(payReq.getBusinessType().getCode());
						orderPay4Update.setMethod(payReq.getMethod().getCode());
						orderPay4Update.setAppid(payReq.getAppid());
						orderPay4Update.setAmount(payReq.getAmount());
						orderPay.setBody(payReq.getBody());
						orderPay4Update.setStatus(OrderPayEnum.Status.WAITPAY.getCode());
						orderPay4Update.setNotifyUrl(payReq.getNotifyUrl());
						orderPay4Update.setNotifyAttach(payReq.getAttach());
						orderPay4Update.setRemark(payReq.getRemark());
						
						orderPayService.updateById(orderPay4Update);
					}

					PayParams payParams = new PayParams();

					// 业务参数
					payParams.setUserId(payReq.getUserId());

					// 支付参数
					payParams.setAppid(payReq.getAppid());
					payParams.setAmount(payReq.getAmount().setScale(2, BigDecimal.ROUND_HALF_UP));// 向上取整，保留2位小数
					payParams.setBody(payReq.getBody());
					payParams.setOutTradeNo(orderCode);
					payParams.setOpenid(payReq.getOpenid());
					
					PayClient tradeClient = PayFactory.of(payReq.getMethod());
					PayResp payResp = tradeClient.pay(payParams);
					payTimeout(orderCode, payReq.getTimeoutSeconds());// 订单超时处理
					
					return payResp;
				});
		return Result.success(payRespR);
	}

	/**
	 * 订单超时未支付延时任务
	 * 
	 * @param orderCode
	 *            订单号
	 * @param timeoutSeconds
	 *            超时秒数
	 */
	private void payTimeout(String orderCode, Integer timeoutSeconds) {
		Map<String, Object> params = Maps.newHashMap();
		params.put("orderCode", orderCode);
		if (timeoutSeconds == null) {
			timeoutSeconds = 1800;// 默认30分钟,1800秒
		}
		params.put("delaySeconds", timeoutSeconds);

		messageSender.sendDelayMessage(StrategyConstants.PAY_TIMEOUT_STRATEGY, params, Constants.EXCHANGE.XDELAYED,
				Constants.QUEUE.XDELAYED.ROUTING_KEY, timeoutSeconds);
	}
	
	@Override
	public Result<PayInfoResp> queryPayInfo(String orderCode) {
		OrderPay orderPay = orderPayService.selectByOrderCode(orderCode);
		if (orderPay == null) {
			return Result.fail("数据不存在");
		}
		PayClient tradeClient = PayFactory.of(OrderPayEnum.Method.of(orderPay.getMethod()));
		Object payInfo = tradeClient.getPayInfo(orderCode);

		PayInfoResp payInfoResp = new PayInfoResp();
		payInfoResp.setPayInfo(payInfo);

		return Result.success(payInfoResp);
	}

	@Override
	public Result<PayTradeStateResp> queryTradeState(String orderCode) {
		OrderPay orderPay = orderPayService.selectByOrderCode(orderCode);
		if (orderPay == null) {
			return Result.fail("数据不存在");
		}
		PayClient tradeClient = PayFactory.of(OrderPayEnum.Method.of(orderPay.getMethod()));
		PayTradeStateResp payTradeState = tradeClient.queryTradeState(orderCode);
		return Result.success(payTradeState);
	}
	
	@Override
	public Result<String> wxPayNotify(@RequestBody String xmlString) {
		/**
		 * <pre>
		<xml>
		  <appid><![CDATA[wx8888888888888888]]></appid>
		  <bank_type><![CDATA[OTHERS]]></bank_type>
		  <cash_fee><![CDATA[2500]]></cash_fee>
		  <fee_type><![CDATA[CNY]]></fee_type>
		  <is_subscribe><![CDATA[N]]></is_subscribe>
		  <mch_id><![CDATA[1900000109]]></mch_id>
		  <nonce_str><![CDATA[cg2gzMG24010243l]]></nonce_str>
		  <openid><![CDATA[oQvXm5SsVx8OjLaZ_zpDJB1rSMss]]></openid>
		  <out_trade_no><![CDATA[203783232649990144]]></out_trade_no>
		  <result_code><![CDATA[SUCCESS]]></result_code>
		  <return_code><![CDATA[SUCCESS]]></return_code>
		  <sign><![CDATA[FCCE0E03BA54987B4BEEEE90DA9361AE]]></sign>
		  <time_end><![CDATA[20201224000006]]></time_end>
		  <total_fee>2500</total_fee>
		  <trade_type><![CDATA[JSAPI]]></trade_type>
		  <transaction_id><![CDATA[4200000808202012244418326253]]></transaction_id>
		</xml>
		 * </pre>
		 */

		// 记录原始数据
		log.info("wx notify data:{}", xmlString);
		PayNotify payNotify = new PayNotify().setMethod(OrderPayEnum.Method.WX.getCode()).setNotifyData(xmlString);
		payNotifyMapper.insert(payNotify);

		// 解析微信支付返回报文
		WxPayOrderNotifyResult orderNotifyResult = WxPayOrderNotifyResult.fromXML(xmlString);

		String outTradeNo = orderNotifyResult.getOutTradeNo();
		if (StringUtils.isBlank(outTradeNo)) {
			payNotifyMapper.updateRemarkById("缺少out_trade_no", payNotify.getId());
			return Result.success(WxPayNotifyResponse.fail("缺少out_trade_no"));
		}

		WxPay wxPay = wxPayMapper.selectByOutTradeNo(outTradeNo);

		// 校验返回结果签名
		Map<String, String> map = orderNotifyResult.toMap();
		if (!SignUtils.checkSign(map, null, wxPay.getMchKey())) {
			payNotifyMapper.updateRemarkById("参数格式校验错误！", payNotify.getId());
			return Result.success(WxPayNotifyResponse.fail("参数格式校验错误！"));
		}

		// 校验返回的订单金额是否与商户侧的订单金额一致
		if (!Objects.equals(wxPay.getTotalFee(), orderNotifyResult.getTotalFee())) {
			payNotifyMapper.updateRemarkById("订单金额不一致", payNotify.getId());
			return Result.success(WxPayNotifyResponse.fail("订单金额不一致"));
		}

		// 回调数据落库
		WxPay wxPay4Update = new WxPay().setNotifyResultCode(orderNotifyResult.getResultCode())
				.setNotifyErrCode(orderNotifyResult.getErrCode()).setNotifyErrCodeDes(orderNotifyResult.getErrCodeDes())
				.setTransactionId(orderNotifyResult.getTransactionId()).setTimeEnd(orderNotifyResult.getTimeEnd())
				.setPayNotifyId(payNotify.getId());

		Wrapper<WxPay> wrapper = new EntityWrapper<WxPay>();
		wrapper.eq("out_trade_no", outTradeNo);
		wrapper.and("(notify_result_code is null or notify_result_code != {0})", WxPayConstants.ResultCode.SUCCESS);
//		wrapper.and(a -> a.isNull("notify_result_code").or().ne("notify_result_code", WxPayConstants.ResultCode.SUCCESS));
		int affect = wxPayMapper.update(wxPay4Update, wrapper);
		if (affect == 0) {
			// 订单回调已处理完成，无需重复处理
			payNotifyMapper.updateRemarkById("订单回调已处理完成，无需重复处理", payNotify.getId());
			return Result.success(WxPayNotifyResponse.success("OK"));
		}

		String resultCode = orderNotifyResult.getResultCode();

		// MQ异步处理
		Map<String, Object> params = Maps.newHashMap();
		params.put("payNotifyId", payNotify.getId());
		params.put("outTradeNo", outTradeNo);

		boolean success = WxPayConstants.ResultCode.SUCCESS.equals(resultCode);
		params.put("success", success);
		if (!success) {
			params.put("message", orderNotifyResult.getErrCodeDes());
		}

		// 财务流水信息
		params.put("amount", new BigDecimal(wxPay.getTotalFee())
				.divide(new BigDecimal(100), 2, BigDecimal.ROUND_HALF_UP).toPlainString());
		params.put("orderPayMethod", OrderPayEnum.Method.WX.getCode());
		params.put("merchantNo", wxPay.getMchid());
		params.put("tradeNo", orderNotifyResult.getTransactionId());

		messageSender.sendNormalMessage(StrategyConstants.PAY_NOTIFY_STRATEGY, params, Constants.EXCHANGE.DIRECT,
				Constants.QUEUE.PAY_NOTIFY.ROUTING_KEY);
		return Result.success(WxPayNotifyResponse.success("OK"));
	}

	@Override
	public Result<String> aliPayNotify(@RequestBody Map<String, String> aliParams) {
		String notifyData = JsonUtil.toJsonString(aliParams);
		// 记录原始数据
		log.info("ali notify data:{}", notifyData);
		PayNotify payNotify = new PayNotify().setMethod(OrderPayEnum.Method.ALI.getCode()).setNotifyData(notifyData);
		payNotifyMapper.insert(payNotify);

		String appid = aliParams.get("app_id");
		AliPayProperties.PayConfig payConfig = AliPayConfiguration.getPayConfig(appid);
		try {
			// 调用SDK验证签名
			boolean signVerified = AlipaySignature.rsaCheckV1(aliParams, payConfig.getPubKey(),
					AliConstants.CHARSET, AliConstants.SIGNTYPE);
			if (!signVerified) {
				payNotifyMapper.updateRemarkById("验签失败", payNotify.getId());
				return Result.success("fail");
			}
		} catch (AlipayApiException e) {
			log.error(">>>解析支付宝回调参数异常，直接返回", e);
			payNotifyMapper.updateRemarkById(e.getMessage(), payNotify.getId());
			return Result.success("fail");
		}

		String tradeStatus = aliParams.get("trade_status");
		String outTradeNo = aliParams.get("out_trade_no");

		if (StringUtils.isNotBlank(aliParams.get("out_biz_no"))) {
			// 如果out_biz_no不为空，则认为是退款回调
			/**
			 * <pre>
			全额退款：
			{
				"gmt_create": "2020-12-28 20:46:00",
				"charset": "UTF-8",
				"seller_email": "485564455@qq.com",
				"subject": "订单-205546307409489920",
				"sign": "gGnICAIp4OyAlnqWgESVFGlPPRfg6NDwblVvC7eZAmnJOBHZKb5OodZHfSs96NM2ff+4sKex5GF6czpzJGZ1VZiYehpZHb6bnoZfRFA5KkoNj5mMkSfzdmnjj/Mmzu5aSz/qUwbtOhF9wR4MUaI1W7xURJNKantewlSRnRAmAbweJgMMVBvNYpGsOkl6wzkXW9GEZPXFQb+NTZeuFcKpPkDurOAGU0aX9YpjE1ouaCxNYqTdsV7+FqTJDaQeYdLWquOFJnDuvxNiC5AOC0m3H19ud8DDvgJi2fvGNkYJ+SlRpPL6sz70dL2HiugnfBEgV0EQJaTTLPiU344BtY68Zg==",
				"body": "app支付",
				"buyer_id": "4011402911110921",
				"notify_id": "2020122800222204607000921452504952",
				"notify_type": "trade_status_sync",
				"trade_status": "TRADE_CLOSED",
				"app_id": "3021451196634505",
				"sign_type": "RSA2",
				"seller_id": "2088923443064778",
				"gmt_payment": "2020-12-28 20:46:00",
				"notify_time": "2020-12-28 20:46:07",
				"gmt_refund": "2020-12-28 20:46:06.659",
				"out_biz_no": "m7X2JM0ohj4aR",
				"version": "1.0",
				"out_trade_no": "205546307409489920",
				"total_amount": "0.01",
				"refund_fee": "0.01",
				"trade_no": "2020122822001400921446208697",
				"auth_app_id": "3021451196634505",
				"gmt_close": "2020-12-28 20:46:06",
				"buyer_logon_id": "187***@139.com"
			}
			部分退款：
			{
				"gmt_create": "2021-12-16 14:57:36",
				"charset": "UTF-8",
				"gmt_payment": "2021-12-16 14:57:37",
				"seller_email": "jqd@163.com",
				"notify_time": "2021-12-16 14:59:30",
				"subject": "订单-333381570281132032",
				"gmt_refund": "2021-12-16 14:59:30.378",
				"sign": "gJcVAwky9Ad02uQsTrS/6pyMflK/gasGFpCB8Jp6RQzr7DnCbzXTzXG17ZrryFqg4gmGe9IwzHr6rxaLV7HyFN29VdUSEwCrG17Kpz3f/avbwctT/hahoz+1prxSmzGmSVCM6QZzIST549wjEYPDyoyDt99UiN4USi3xEeqg/gQggYWm5KyjuttSuNTbRa5zjZUJQtMcXauQvgkM9cGrmJMFyvDcBNegki9drPsEZE9m7Cvm+bf2xZ3UtgkRlJYM7ZhDNgVIDa3IMGRBN6XN5h0+Y5rUA+iEqvXpgqMfBPGAG7K43cMTNFcdZsfFiXxIT15VQm77ENek+PxdymHkZQ==",
				"body": "权益商品购买",
				"buyer_id": "2088412687134912",
				"out_biz_no": "333381966069309440",
				"version": "1.0",
				"notify_id": "2021121600222145930034911423265143",
				"notify_type": "trade_status_sync",
				"out_trade_no": "333381570281132032",
				"total_amount": "0.02",
				"trade_status": "TRADE_SUCCESS",
				"refund_fee": "0.01",
				"trade_no": "2021121622001434911451728350",
				"auth_app_id": "3021451196634505",
				"buyer_logon_id": "152****3215",
				"app_id": "3021451196634505",
				"sign_type": "RSA2",
				"seller_id": "2088923443064778"
			}
			 * </pre>
			 */

			// 回调数据落库
			String outRequestNo = aliParams.get("out_biz_no");
			AliPayRefund aliPay4Update = new AliPayRefund().setTradeStatus(tradeStatus).setOutBizNo(outRequestNo)
					.setPayNotifyId(payNotify.getId());

			Wrapper<AliPayRefund> wrapper = new EntityWrapper<AliPayRefund>();
			wrapper.eq("out_request_no", outRequestNo);
			wrapper.isNull("out_biz_no");
			int affect = aliPayRefundMapper.update(aliPay4Update, wrapper);
			if (affect == 0) {
				// 订单回调已处理完成，无需重复处理
				payNotifyMapper.updateRemarkById("订单回调已处理完成，无需重复处理", payNotify.getId());
				return Result.success("success");
			}

			// MQ异步处理
			Map<String, Object> params = Maps.newHashMap();
			params.put("payNotifyId", payNotify.getId());
			params.put("outTradeNo", outTradeNo);
			params.put("outRefundNo", outRequestNo);
			params.put("success", true);

			//财务流水信息
			params.put("amount", aliParams.get("refund_fee"));
			params.put("orderPayMethod", OrderPayEnum.Method.ALI.getCode());
			params.put("merchantNo", aliParams.get("seller_id"));
			params.put("tradeNo", aliParams.get("trade_no"));

			messageSender.sendNormalMessage(StrategyConstants.REFUND_NOTIFY_STRATEGY, params, Constants.EXCHANGE.DIRECT,
					Constants.QUEUE.COMMON.ROUTING_KEY);
		} else if ("TRADE_SUCCESS".equals(tradeStatus)) {
			// trade_status=TRADE_SUCCESS，则认为是支付成功回调
			/**
			 * <pre>
			{
				"gmt_create": "2020-12-28 20:46:00",
				"charset": "UTF-8",
				"seller_email": "485564455@qq.com",
				"subject": "订单-205546307409489920",
				"sign": "bTcikFIOpTcdY1TAv9+g4dy+7ctVpcJmW/7SxrE80ZgG1uXGvj/6JH+tMtpkIxxDl2Wzz/NRq82cACVfDO1WIjeRWhCSx/QKASBijrnLDuUP9LJxCNQlGVUjW3fk9N1GzE8NaCeedpyUHX4ldNW3T3F8t+vcQSF9v6uSucNDFJpdIo/10YT9l/x/5voV82+oFJGFEWYXhT2IyWFspcCD0Wn/5wRuoR67IWDTgzm+w+jF9++XhfNY2mTKcbfvQZCb6B6sQA/SkCLY4ErY7ZkIwlQ70JgdyR3A/adVrbzhkiwszn8B6gTkgbx6hxQSr3AU61V3PjjQfFL1JN7DCMLlTA==",
				"body": "app支付",
				"buyer_id": "4011402911110921",
				"invoice_amount": "0.01",
				"notify_id": "2020122800222204600000921452547067",
				"fund_bill_list": "[{\"amount\":\"0.01\",\"fundChannel\":\"PCREDIT\"}]",
				"notify_type": "trade_status_sync",
				"trade_status": "TRADE_SUCCESS",
				"receipt_amount": "0.01",
				"app_id": "3021451196634505",
				"buyer_pay_amount": "0.01",
				"sign_type": "RSA2",
				"seller_id": "2088923443064778",
				"gmt_payment": "2020-12-28 20:46:00",
				"notify_time": "2020-12-28 20:46:01",
				"version": "1.0",
				"out_trade_no": "205546307409489920",
				"total_amount": "0.01",
				"trade_no": "2020122822001400921446208697",
				"auth_app_id": "3021451196634505",
				"buyer_logon_id": "187***@139.com",
				"point_amount": "0.00"
			}
			 * </pre>
			 */

			// 回调数据落库
			AliPay aliPay4Update = new AliPay().setTradeStatus(tradeStatus).setTradeNo(aliParams.get("trade_no"))
					.setGmtPayment(aliParams.get("gmt_payment")).setPayNotifyId(payNotify.getId());

			Wrapper<AliPay> wrapper = new EntityWrapper<AliPay>();
			wrapper.eq("out_trade_no", outTradeNo);
			wrapper.and("(trade_status is null or trade_status != {0})", AliConstants.TRADE_SUCCESS);
//			wrapper.and(a -> a.isNull("trade_status").or().ne("trade_status", AliConstants.TRADE_SUCCESS));
			int affect = aliPayMapper.update(aliPay4Update, wrapper);
			if (affect == 0) {
				// 订单回调已处理完成，无需重复处理
				payNotifyMapper.updateRemarkById("订单回调已处理完成，无需重复处理", payNotify.getId());
				return Result.success("success");
			}

			// MQ异步处理
			Map<String, Object> params = Maps.newHashMap();
			params.put("payNotifyId", payNotify.getId());
			params.put("outTradeNo", outTradeNo);
			params.put("success", true);

			//财务流水信息
			params.put("amount", aliParams.get("total_amount"));
			params.put("orderPayMethod", OrderPayEnum.Method.ALI.getCode());
			params.put("merchantNo", aliParams.get("seller_id"));
			params.put("tradeNo", aliParams.get("trade_no"));

			messageSender.sendNormalMessage(StrategyConstants.PAY_NOTIFY_STRATEGY, params, Constants.EXCHANGE.DIRECT,
					Constants.QUEUE.PAY_NOTIFY.ROUTING_KEY);
		}
		return Result.success("success");
	}

	@Override
	public Result<Void> refund(@RequestBody @Valid PayRefundReq payRefundReq) {
		String outTradeNo = payRefundReq.getOrderCode();
		OrderPay orderPay = orderPayService.selectByOrderCode(outTradeNo);
		if (orderPay == null) {
			return Result.fail("订单不存在");
		}
		
		if (OrderPayEnum.Status.of(orderPay.getStatus()) != OrderPayEnum.Status.PAYED) {
			// 原订单不是已支付状态，不允许退款
			return Result.fail("订单不是完成状态，不允许退款");
		}

		String outRefundNo = payRefundReq.getRefundOrderCode();
		String key = String.format("lock:orderrefund:ordercode:%s", outRefundNo);
		String message = DistributeLockUtils
				.doInDistributeLockThrow(key, () -> {
					OrderPayRefund refundOrderPay = orderPayRefundService.selectByRefundOrderCode(outRefundNo);
					if (refundOrderPay != null) {
						return null;
					}
					// 已退款金额是否已达到订单总金额
					BigDecimal sumRefundAmount = orderPayRefundService.sumRefundAmount(outTradeNo);
					if (orderPay.getAmount().compareTo(sumRefundAmount) <= 0) {
						return "已全额退款";
					}

					BigDecimal leftAmount = orderPay.getAmount().subtract(sumRefundAmount);// 剩余金额
					// 退款金额与剩余金额比较
					BigDecimal refundAmount = payRefundReq.getRefundAmount();
					if (refundAmount != null) {
						if (refundAmount.compareTo(leftAmount) > 0) {
							return "退款金额超出可退款金额";
						}
					} else {
						refundAmount = leftAmount;
					}

					// 创建退款订单
					refundOrderPay = new OrderPayRefund();
					refundOrderPay.setUserId(orderPay.getUserId());
					refundOrderPay.setBusinessType(payRefundReq.getBusinessType().getCode());
					refundOrderPay.setMethod(orderPay.getMethod());
					refundOrderPay.setOrderCode(outTradeNo);
					refundOrderPay.setRefundOrderCode(outRefundNo);
					refundOrderPay.setAmount(orderPay.getAmount());
					refundOrderPay.setRefundAmount(refundAmount);
					refundOrderPay.setStatus(OrderPayRefundEnum.Status.WAIT_APPLY.getCode());
					refundOrderPay.setRemark(payRefundReq.getRefundRemark());
					refundOrderPay.setNotifyUrl(payRefundReq.getNotifyUrl());
					refundOrderPay.setNotifyAttach(payRefundReq.getAttach());
					orderPayRefundService.insert(refundOrderPay);
					return null;
				});

		if (message != null) {
			return Result.fail(message);
		}

		// 异步处理 ========> 逻辑上等同于直接调用 refundWithRetry
		RefundReq refundReq = new RefundReq();
		refundReq.setOutRefundNo(outRefundNo);

		ProcessorBeanName processorBeanName = new ProcessorBeanName();
		processorBeanName.setAbandonRequest(InnerCallbackProcessorBeanName.REFUND_FAIL_PROCESSOR);

		innerCallbackService.postRestTemplate(NOTIFY_URL_REFUND, refundReq, processorBeanName);
		
		return Result.success();
	}
	
	@Override
	public Result<Void> refundWithRetry(@RequestBody RefundReq refundReq) {
		OrderPayRefund refundOrderPay = orderPayRefundService.selectByRefundOrderCode(refundReq.getOutRefundNo());

		if (refundOrderPay == null) {
			return Result.fail("数据不存在");
		}

		String outTradeNo = refundOrderPay.getOrderCode();
		BigDecimal refundAmount = refundOrderPay.getRefundAmount().setScale(2, BigDecimal.ROUND_HALF_UP);// 向上取整，保留2位小数

		PayClient payClient = PayFactory.of(OrderPayEnum.Method.of(refundOrderPay.getMethod()));
		PayRefundResp payRefundResp = payClient.refund(outTradeNo, refundOrderPay.getOrderCode(), refundAmount);

		if (!payRefundResp.getSuccess()) {
			return Result.fail(payRefundResp.getMessage());
		}

		return Result.success();
	}
	
	@Override
	public Result<String> wxPayRefundNotify(@RequestBody String xmlString) {
		/**
		 * <pre>
		 * 官方文档：https://pay.weixin.qq.com/wiki/doc/api/H5.php?chapter=9_16&index=10
		<xml>
		  <return_code>SUCCESS</return_code>
		  <appid><![CDATA[wx8888888888888888]]></appid>
		  <mch_id><![CDATA[1900000109]]></mch_id>
		  <nonce_str><![CDATA[5K8264ILTKCH16CQ2502SI8ZNMTM67VS]]></nonce_str>
		  <req_info><![CDATA[T87GAHG17TGAHG1TGHAHAHA1Y1CIOA9UGJH1GAHV871HAGAGQYQQPOOJMXNBCXBVNMNMAJAA]]></req_info>
		  <return_msg><![CDATA[90]]></return_msg>
		</xml>
		
		req_info解密后的示例： 
		<root>
		  <out_refund_no><![CDATA[131811191610442717309]]></out_refund_no>
		  <out_trade_no><![CDATA[71106718111915575302817]]></out_trade_no>
		  <refund_account><![CDATA[REFUND_SOURCE_RECHARGE_FUNDS]]></refund_account>
		  <refund_fee><![CDATA[3960]]></refund_fee>
		  <refund_id><![CDATA[50000408942018111907145868882]]></refund_id>
		  <refund_recv_accout><![CDATA[支付用户零钱]]></refund_recv_accout>
		  <refund_request_source><![CDATA[API]]></refund_request_source>
		  <refund_status><![CDATA[SUCCESS]]></refund_status>
		  <settlement_refund_fee><![CDATA[3960]]></settlement_refund_fee>
		  <settlement_total_fee><![CDATA[3960]]></settlement_total_fee>
		  <success_time><![CDATA[2018-11-19 16:24:13]]></success_time>
		  <total_fee><![CDATA[3960]]></total_fee>
		  <transaction_id><![CDATA[4200000215201811190261405420]]></transaction_id>
		  <cash_refund_fee><![CDATA[90]]></cash_refund_fee>
		</root>
		 * </pre>
		 */
		// 记录原始数据
		log.info("wx refund notify data:{}", xmlString);
		PayNotify payNotify = new PayNotify().setMethod(OrderPayEnum.Method.WX.getCode()).setNotifyData(xmlString);
		payNotifyMapper.insert(payNotify);

		// 解析微信支付返回报文
		WxPayRefundNotifyResult refundNotifyResult = BaseWxPayResult.fromXML(xmlString, WxPayRefundNotifyResult.class);

		String mchId = refundNotifyResult.getMchId();
		if (StringUtils.isBlank(mchId)) {
			payNotifyMapper.updateRemarkById("缺少mch_id", payNotify.getId());
			return Result.success(WxPayNotifyResponse.fail("缺少mch_id"));
		}
		
		String appid = refundNotifyResult.getAppid();
		if (StringUtils.isBlank(appid)) {
			payNotifyMapper.updateRemarkById("缺少appid", payNotify.getId());
			return Result.success(WxPayNotifyResponse.fail("缺少appid"));
		}
		
		WxPayProperties.PayConfig payConfig = WxPayConfiguration.getPayConfig(appid);
		String mchKey = payConfig.getMchKey();

		try {
			// 校验返回结果签名
			refundNotifyResult = WxPayRefundNotifyResult.fromXML(xmlString, mchKey);
		} catch (WxPayException e) {
			log.error("校验返回结果签名异常", e);
			payNotifyMapper.updateRemarkById(e.getMessage(), payNotify.getId());
			return Result.success(WxPayNotifyResponse.fail(e.getMessage()));
		}

		ReqInfo reqInfo = refundNotifyResult.getReqInfo();
		String outRefundNo = reqInfo.getOutRefundNo();

		// 回调数据落库
		WxPayRefund wxPay4Update = new WxPayRefund().setRefundStatus(reqInfo.getRefundStatus())
				.setPayNotifyId(payNotify.getId());

		Wrapper<WxPayRefund> wrapper = new EntityWrapper<WxPayRefund>();
		wrapper.eq("out_refund_no", outRefundNo);
		wrapper.and("(refund_status is null or refund_status != {0})", WxPayConstants.RefundStatus.SUCCESS);
//		wrapper.and(a -> a.isNull("refund_status").or().ne("refund_status", WxPayConstants.RefundStatus.SUCCESS));
		int affect = wxPayRefundMapper.update(wxPay4Update, wrapper);
		if (affect == 0) {
			// 订单回调已处理完成，无需重复处理
			payNotifyMapper.updateRemarkById("订单回调已处理完成，无需重复处理", payNotify.getId());
			return Result.success(WxPayNotifyResponse.success("OK"));
		}

		String refundStatus = reqInfo.getRefundStatus();

		// MQ异步处理
		Map<String, Object> params = Maps.newHashMap();
		params.put("payNotifyId", payNotify.getId());
		params.put("outTradeNo", reqInfo.getOutTradeNo());
		params.put("outRefundNo", outRefundNo);

		boolean success = WxPayConstants.RefundStatus.SUCCESS.equals(refundStatus);
		params.put("success", success);
		if (!success) {
			params.put("message", refundNotifyResult.getErrCodeDes());
		}

		//财务流水信息
		params.put("amount", new BigDecimal(reqInfo.getRefundFee())
				.divide(new BigDecimal(100), 2, BigDecimal.ROUND_HALF_UP).toPlainString());
		params.put("merchantNo", refundNotifyResult.getMchId());
		params.put("orderPayMethod", OrderPayEnum.Method.WX.getCode());
		params.put("tradeNo", reqInfo.getTransactionId());

		messageSender.sendNormalMessage(StrategyConstants.REFUND_NOTIFY_STRATEGY, params, Constants.EXCHANGE.DIRECT,
				Constants.QUEUE.COMMON.ROUTING_KEY);
		return Result.success(WxPayNotifyResponse.success("OK"));
	}

	@Override
	public Result<Void> payClose(@RequestBody @Valid PayCloseReq payCloseReq) {
		OrderPay orderPay = orderPayService.selectByOrderCode(payCloseReq.getOrderCode());

		if (orderPay == null) {
			return Result.fail("数据不存在");
		}
		OrderPayEnum.Status status = OrderPayEnum.Status.of(orderPay.getStatus());
		if (status != OrderPayEnum.Status.WAITPAY) {
			// 订单不是未支付状态，不允许关闭
			return Result.fail("订单不是待支付状态，不允许关闭");
		}

		Map<String, Object> params = new HashMap<>();
		params.put("outTradeNo", payCloseReq.getOrderCode());
		
		Date minPayCloseTime = DateUtils.addMinutes(orderPay.getCreateTime(), 5);
		Date now = new Date();
		// 微信订单不能创建后马上关闭，延迟调用微信关闭接口时间 单位：s
		// 官方文档：https://pay.weixin.qq.com/wiki/doc/api/H5.php?chapter=9_3&index=3
		Integer delay = 0;
		if (minPayCloseTime.compareTo(now) > 0) {
			delay = (int) (minPayCloseTime.getTime() - now.getTime()) / 1000;
		}
		
		messageSender.sendDelayMessage(StrategyConstants.PAY_CLOSE_STRATEGY, params, Constants.EXCHANGE.XDELAYED,
				Constants.QUEUE.XDELAYED.ROUTING_KEY, delay);
		
		return Result.success();
	}
}
