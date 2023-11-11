package com.company.order.controller;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alipay.api.domain.SaleActivityInfo;
import com.company.common.api.Result;
import com.company.common.util.JsonUtil;
import com.company.framework.context.HttpContextUtil;
import com.company.order.api.enums.OrderPayEnum;
import com.company.order.api.enums.OrderPayRefundEnum;
import com.company.order.api.feign.TestFeign;
import com.company.order.api.request.PayNotifyReq;
import com.company.order.api.request.PayRefundReq;
import com.company.order.api.request.PayReq;
import com.company.order.api.request.RefundNotifyReq;
import com.company.order.api.response.PayInfoResp;
import com.company.order.api.response.PayResp;
import com.google.common.collect.Lists;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping(value = "/test")
public class TestController implements TestFeign {

	@Autowired
	private PayController payFeign;

	@Override
	public Result<PayResp> minipay(String orderCode) {
		PayReq payReq = new PayReq();
		Integer userId = 1;
		payReq.setUserId(userId);
		payReq.setOrderCode(orderCode);
		payReq.setBusinessType(OrderPayEnum.BusinessType.NOMAL);
		payReq.setMethod(OrderPayEnum.Method.WX);
		payReq.setAppid("wxeb6ffb3sdaddad1d");
		payReq.setAmount(new BigDecimal("1"));
		payReq.setBody("下单");
		payReq.setSpbillCreateIp(HttpContextUtil.requestip());
		payReq.setOpenid("oQvXm5d0q3sadasdunR1Y-oEr3ZmQ");
		payReq.setNotifyUrl("http://template-order/test/buyNotify");
		// payReq.setAttach(attach);
		payReq.setTimeoutSeconds(60);

		PayResp payResp = payFeign.unifiedorder(payReq).dataOrThrow();
		return Result.success(payResp);
	}
	
	@Override
	public Result<PayResp> apppay(String orderCode) {
		PayReq payReq = new PayReq();
		Integer userId = 1;
		payReq.setUserId(userId);
		payReq.setOrderCode(orderCode);
		payReq.setBusinessType(OrderPayEnum.BusinessType.NOMAL);
		payReq.setMethod(OrderPayEnum.Method.WX);
		payReq.setAppid("wx79c0easdsadba516");
		payReq.setAmount(new BigDecimal("1"));
		payReq.setBody("下单");
		payReq.setSpbillCreateIp(HttpContextUtil.requestip());
		// payReq.setOpenid(openid);
		payReq.setNotifyUrl("http://template-order/test/buyNotify");
		// payReq.setAttach(attach);
		payReq.setTimeoutSeconds(60);

		PayResp payResp = payFeign.unifiedorder(payReq).dataOrThrow();
		return Result.success(payResp);
	}
	
	@Override
	public Result<PayResp> gzhpay(String orderCode) {
		PayReq payReq = new PayReq();
		Integer userId = 1;
		payReq.setUserId(userId);
		payReq.setOrderCode(orderCode);
		payReq.setBusinessType(OrderPayEnum.BusinessType.NOMAL);
		payReq.setMethod(OrderPayEnum.Method.WX);
		payReq.setAppid("wx93asdsadddb142b");
		payReq.setAmount(new BigDecimal("1"));
		payReq.setBody("下单");
		payReq.setSpbillCreateIp(HttpContextUtil.requestip());
		payReq.setOpenid("oQvXm5d0q3sadasdunR1Y-oEr3ZmQ");
		payReq.setNotifyUrl("http://template-order/test/buyNotify");
		// payReq.setAttach(attach);
		payReq.setTimeoutSeconds(60);
		
		PayResp payResp = payFeign.unifiedorder(payReq).dataOrThrow();
		return Result.success(payResp);
	}
	
	@Override
	public Result<PayResp> mwebpay(String orderCode) {
		PayReq payReq = new PayReq();
		Integer userId = 1;
		payReq.setUserId(userId);
		payReq.setOrderCode(orderCode);
		payReq.setBusinessType(OrderPayEnum.BusinessType.NOMAL);
		payReq.setMethod(OrderPayEnum.Method.WX);
		payReq.setAppid("wx93asdsadddb142b");
		payReq.setAmount(new BigDecimal("1"));
		payReq.setBody("下单");
		payReq.setSpbillCreateIp(HttpContextUtil.requestip());
		// payReq.setOpenid(openid);
		payReq.setNotifyUrl("http://template-order/test/buyNotify");
		// payReq.setAttach(attach);
		payReq.setTimeoutSeconds(60);
		
		PayResp payResp = payFeign.unifiedorder(payReq).dataOrThrow();
		return Result.success(payResp);
	}
	
	@Override
	public Result<PayResp> alipay(String orderCode) {
		PayReq payReq = new PayReq();
		Integer userId = 1;
		payReq.setUserId(userId);
		payReq.setOrderCode(orderCode);
		payReq.setBusinessType(OrderPayEnum.BusinessType.NOMAL);
		payReq.setMethod(OrderPayEnum.Method.ALI);
		payReq.setAppid("2021034111110503");
		payReq.setAmount(new BigDecimal("1"));
		payReq.setBody("下单");
		payReq.setSpbillCreateIp(HttpContextUtil.requestip());
		// payReq.setOpenid(openid);
		payReq.setNotifyUrl("http://template-order/test/buyNotify");
		// payReq.setAttach(attach);
		payReq.setTimeoutSeconds(60);
		
		PayResp payResp = payFeign.unifiedorder(payReq).dataOrThrow();
		return Result.success(payResp);
	}
	
	@Override
	public Result<PayResp> aliactivitypay(String orderCode) {
		PayReq payReq = new PayReq();
		Integer userId = 1;
		payReq.setUserId(userId);
		payReq.setOrderCode(orderCode);
		payReq.setBusinessType(OrderPayEnum.BusinessType.NOMAL);
		payReq.setMethod(OrderPayEnum.Method.ALIACTIVITY);
		payReq.setAppid("2021003111112476");
		payReq.setAmount(new BigDecimal("1.20"));
		
		List<SaleActivityInfo> saleActivityInfoList = Lists.newArrayList();
		SaleActivityInfo saleActivityInfo = new SaleActivityInfo();
		saleActivityInfo.setActivityId("2022122900826004776420383498");
		saleActivityInfo.setAmount("1.20");
		saleActivityInfo.setQuantity(1L);
		
		saleActivityInfoList.add(saleActivityInfo);
		payReq.setBody(JsonUtil.toJsonString(saleActivityInfoList));
		
		payReq.setOpenid("2088412687134912");
		payReq.setNotifyUrl("http://template-order/test/buyNotify");
		// payReq.setAttach(attach);
		payReq.setTimeoutSeconds(60);
		
		PayResp payResp = payFeign.unifiedorder(payReq).dataOrThrow();
		return Result.success(payResp);
	}

	@Override
	public Result<Void> buyNotify(@RequestBody PayNotifyReq payNotifyReq) {
		log.info("payNotifyReq:{}", JsonUtil.toJsonString(payNotifyReq));
		return Result.success();
	}
	
	@Override
	public Result<PayInfoResp> queryPayInfo(String orderCode) {
		PayInfoResp payInfoResp = payFeign.queryPayInfo(orderCode).dataOrThrow();
		return Result.success(payInfoResp);
	}
	
	@Override
	public Result<PayInfoResp> refund(String orderCode, String refundOrderCode) {
		PayRefundReq payRefundReq = new PayRefundReq();
		payRefundReq.setOrderCode(orderCode);
		payRefundReq.setRefundOrderCode(refundOrderCode);
		payRefundReq.setBusinessType(OrderPayRefundEnum.BusinessType.USER);
		payRefundReq.setRefundAmount(new BigDecimal("1"));
		payRefundReq.setNotifyUrl("http://template-order/test/refundNotify");
//		payRefundReq.setAttach(attach);
		payRefundReq.setRefundRemark("退款");
		
		payFeign.refund(payRefundReq).dataOrThrow();
		return Result.success();
	}
	
	@Override
	public Result<Void> refundNotify(@RequestBody RefundNotifyReq refundNotifyReq) {
		log.info("refundNotifyReq:{}", JsonUtil.toJsonString(refundNotifyReq));
		return Result.success();
	}
}
