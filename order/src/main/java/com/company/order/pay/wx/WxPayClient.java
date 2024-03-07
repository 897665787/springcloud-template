package com.company.order.pay.wx;

import java.math.BigDecimal;
import java.util.Optional;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import com.company.common.exception.BusinessException;
import com.company.common.util.MdcUtil;
import com.company.framework.context.SpringContextUtil;
import com.company.order.api.response.PayTradeStateResp;
import com.company.order.entity.WxPay;
import com.company.order.entity.WxPayRefund;
import com.company.order.mapper.WxPayMapper;
import com.company.order.mapper.WxPayRefundMapper;
import com.company.order.pay.PayFactory;
import com.company.order.pay.core.BasePayClient;
import com.company.order.pay.dto.PayParams;
import com.company.order.pay.wx.config.WxPayConfiguration;
import com.company.order.pay.wx.config.WxPayProperties;
import com.company.order.pay.wx.config.WxPayProperties.PayConfig;
import com.company.order.pay.wx.mock.NotifyMock;
import com.github.binarywang.wxpay.bean.request.WxPayOrderCloseRequest;
import com.github.binarywang.wxpay.bean.request.WxPayOrderQueryRequest;
import com.github.binarywang.wxpay.bean.request.WxPayRefundRequest;
import com.github.binarywang.wxpay.bean.request.WxPayUnifiedOrderRequest;
import com.github.binarywang.wxpay.bean.result.BaseWxPayResult;
import com.github.binarywang.wxpay.bean.result.WxPayOrderCloseResult;
import com.github.binarywang.wxpay.bean.result.WxPayOrderQueryResult;
import com.github.binarywang.wxpay.bean.result.WxPayRefundResult;
import com.github.binarywang.wxpay.bean.result.WxPayUnifiedOrderResult;
import com.github.binarywang.wxpay.config.WxPayConfig;
import com.github.binarywang.wxpay.constant.WxPayConstants;
import com.github.binarywang.wxpay.constant.WxPayConstants.TradeType;
import com.github.binarywang.wxpay.constant.WxPayErrorCode;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.github.binarywang.wxpay.service.WxPayService;
import com.github.binarywang.wxpay.service.impl.WxPayServiceImpl;
import com.google.common.base.Objects;

import lombok.extern.slf4j.Slf4j;

/**
 * 微信支付
 */
@Slf4j
@Component(PayFactory.WX_PAYCLIENT)
public class WxPayClient extends BasePayClient {

	private static final String PAY_CALLBACK_URL = "/server/callback/wx";
	private static final String REFUND_CALLBACK_URL = "/server/callback/wxRefund";
    
	@Autowired
	private WxPayConfiguration wxPayConfiguration;
	@Autowired
	private WxPayMapper wxPayMapper;
	@Autowired
	private WxPayRefundMapper wxPayRefundMapper;
	@Autowired
	private ThreadPoolTaskExecutor executor;
	
	@Value("${template.domain}")
	private String domain;
	
	@Value("${template.mock.wxnotify:false}")
	private Boolean mockNotify;
	
	@Override
    protected Object requestPayInfo(PayParams payParams) {
		WxPay wxPay = wxPayMapper.selectByOutTradeNo(payParams.getOutTradeNo());
		
		PayConfig payConfig = wxPayConfiguration.getPayConfig(payParams.getAppid());
		
		String tradeType = payConfig.getTradeType();
		Integer wxPayId = null;
		if (wxPay != null) {// 已下过单
			if (WxPayConstants.ResultCode.SUCCESS.equals(wxPay.getResultCode())) {// 已成功下过单
				OrderResultTransfer orderResultTransfer = SpringContextUtil
						.getBean(OrderResultTransfer.BEAN_NAME_PREFIX + tradeType, OrderResultTransfer.class);
				Object payInfo = orderResultTransfer.toPayInfo(wxPay.getAppid(), wxPay.getMchid(),
						wxPay.getPrepayId(), wxPay.getCodeUrl(), wxPay.getMwebUrl());
				return payInfo;
			}
			wxPayId = wxPay.getId();
		}
		
		// 公共参数
		WxPayConfig wxPayConfig = new WxPayConfig();
		wxPayConfig.setAppId(payConfig.getAppId());
		wxPayConfig.setMchId(payConfig.getMchId());
		WxPayProperties.MchConfig mchConfig = wxPayConfiguration.getMchConfig(payConfig.getMchId());
		wxPayConfig.setMchKey(mchConfig.getMchKey());
		
		// 私有参数
		WxPayUnifiedOrderRequest request = WxPayUnifiedOrderRequest.newBuilder().build();
		request.setBody(payParams.getBody());
		request.setTotalFee(payParams.getAmount().multiply(new BigDecimal(100)).intValue());
		request.setOutTradeNo(payParams.getOutTradeNo());
		request.setSpbillCreateIp(payParams.getSpbillCreateIp());
		request.setNotifyUrl(domain + PAY_CALLBACK_URL);
		request.setTradeType(tradeType);
		request.setOpenid(payParams.getOpenid());
		request.setProductId(payParams.getProductId());
		request.setNonceStr(RandomStringUtils.randomAlphanumeric(16));
		if (TradeType.MWEB.equals(tradeType)) {
			request.setSceneInfo(
					"{\"h5_info\": {\"type\":\"Wap\",\"wap_url\": \"" + domain + "\",\"wap_name\": \"业务名称\"}}");
		}
		
		try {
			WxPayService wxPayService = new WxPayServiceImpl();
			wxPayService.setConfig(wxPayConfig);
			
			WxPayUnifiedOrderResult unifiedOrderResult = wxPayService.unifiedOrder(request);
			
			requestResult2WxPay(wxPayId, payConfig, request, unifiedOrderResult, null);

			String returnCode = unifiedOrderResult.getReturnCode();
			if (!Objects.equal(returnCode, WxPayConstants.ResultCode.SUCCESS)) {
				throw new BusinessException(unifiedOrderResult.getReturnMsg());
			}
			
			String resultCode = unifiedOrderResult.getResultCode();
			if (!Objects.equal(resultCode, WxPayConstants.ResultCode.SUCCESS)) {
				throw new BusinessException(unifiedOrderResult.getErrCodeDes());
			}

			OrderResultTransfer orderResultTransfer = SpringContextUtil
					.getBean(OrderResultTransfer.BEAN_NAME_PREFIX + tradeType, OrderResultTransfer.class);
			Object payInfo = orderResultTransfer.toPayInfo(request.getAppid(), request.getMchId(),
					unifiedOrderResult.getPrepayId(), unifiedOrderResult.getCodeURL(),
					unifiedOrderResult.getMwebUrl());
			
			if (mockNotify && !SpringContextUtil.isProduceProfile()) {
				executor.submit(() -> {
					try {
						Thread.sleep(10000);// 10s后回调
					} catch (InterruptedException e) {
						log.error("sleep error", e);
					}
					log.info("模拟微信支付回调入口");
					NotifyMock.payNotify(wxPayConfig, request);
				});
			}
			return payInfo;
		} catch (WxPayException e) {
			// 支付异常
			log.error("WxPay error", e);
			WxPayUnifiedOrderResult result = BaseWxPayResult.fromXML(e.getXmlString(), WxPayUnifiedOrderResult.class);
			requestResult2WxPay(wxPayId, payConfig, request, result, "请求异常,logid:" + MdcUtil.get());
			throw new BusinessException(Optional.ofNullable(e.getErrCodeDes()).orElse(e.getReturnMsg()));
		}
	}

	private void requestResult2WxPay(Integer wxPayId, PayConfig payConfig, WxPayUnifiedOrderRequest request,
			WxPayUnifiedOrderResult result, String remark) {
		// 保存微信支付数据
		WxPay wxPay = new WxPay()
				/* 支付参数 */
				.setAppid              (payConfig.getAppId())
				.setMchid              (payConfig.getMchId())
				.setNonceStr           (request.getNonceStr())
				.setSign           	(request.getSign())
				.setBody               (request.getBody())
				.setOutTradeNo         (request.getOutTradeNo())
				.setTotalFee           (request.getTotalFee())
				.setSpbillCreateIp     (request.getSpbillCreateIp())
				.setNotifyUrl          (request.getNotifyUrl())
				.setTradeType          (request.getTradeType())
				.setProductId         (request.getProductId())
				.setOpenid             (request.getOpenid())
				;
    	
		if (result != null) {
			/* 支付信息 */
			wxPay.setReturnCode(result.getReturnCode());
			wxPay.setReturnMsg(result.getReturnMsg());
			wxPay.setResultCode(result.getResultCode());
			wxPay.setErrCode(result.getErrCode());
			wxPay.setErrCodeDes(result.getErrCodeDes());
			wxPay.setPrepayId(result.getPrepayId());
			wxPay.setMwebUrl(result.getMwebUrl());
			wxPay.setCodeUrl(result.getCodeURL());
		}
		wxPay.setRemark(remark);

		if (wxPayId == null) {
			wxPayMapper.insert(wxPay);
		} else {
			wxPay.setId(wxPayId);
			wxPayMapper.updateById(wxPay);
		}
	}

	@Override
	public PayTradeStateResp queryTradeState(String outTradeNo) {
		PayTradeStateResp payTradeStateResp = new PayTradeStateResp();
		WxPay wxPay = wxPayMapper.selectByOutTradeNo(outTradeNo);
		if (wxPay != null && wxPay.getNotifyResultCode() != null) {
			// 回调结果
			payTradeStateResp.setResult(true);
			payTradeStateResp.setMessage(wxPay.getNotifyErrCodeDes());
			payTradeStateResp
					.setPaySuccess(Objects.equal(wxPay.getNotifyResultCode(), WxPayConstants.WxpayTradeStatus.SUCCESS));
			return payTradeStateResp;
		}
		
		// 公共参数
		WxPayConfig wxPayConfig = new WxPayConfig();
		wxPayConfig.setAppId(wxPay.getAppid());
		wxPayConfig.setMchId(wxPay.getMchid());
		WxPayProperties.MchConfig mchConfig = wxPayConfiguration.getMchConfig(wxPay.getMchid());
		wxPayConfig.setMchKey(mchConfig.getMchKey());
		
		try {
			WxPayService wxPayService = new WxPayServiceImpl();
			wxPayService.setConfig(wxPayConfig);

			// 私有参数
			WxPayOrderQueryRequest request = WxPayOrderQueryRequest.newBuilder().build();
			request.setOutTradeNo(wxPay.getOutTradeNo());
			request.setNonceStr(RandomStringUtils.randomAlphanumeric(16));
			
			WxPayOrderQueryResult orderQueryResult = wxPayService.queryOrder(request);

			String returnCode = orderQueryResult.getReturnCode();
			if (!Objects.equal(returnCode, WxPayConstants.ResultCode.SUCCESS)) {
				payTradeStateResp.setResult(false);
				payTradeStateResp.setMessage(orderQueryResult.getReturnMsg());
				return payTradeStateResp;
			}

			String resultCode = orderQueryResult.getResultCode();
			if (!Objects.equal(resultCode, WxPayConstants.ResultCode.SUCCESS)) {
				payTradeStateResp.setResult(false);
				payTradeStateResp.setMessage(orderQueryResult.getErrCodeDes());
				return payTradeStateResp;
			}
			payTradeStateResp.setResult(true);
			payTradeStateResp.setMessage(orderQueryResult.getTradeStateDesc());
			payTradeStateResp.setPaySuccess(
					Objects.equal(orderQueryResult.getTradeState(), WxPayConstants.WxpayTradeStatus.SUCCESS));
			return payTradeStateResp;
		} catch (WxPayException e) {
			// 查询异常
			log.error("WxPay queryTradeState error", e);
			throw new BusinessException(Optional.ofNullable(e.getErrCodeDes()).orElse(e.getReturnMsg()));
		}
	}
	
	@Override
	public void requestRefund(String outTradeNo, String outRefundNo, BigDecimal refundAmount) {
		WxPayRefund wxPayRefund = wxPayRefundMapper.selectByOutRefundNo(outRefundNo);
		Integer wxPayRefundId = null;
		if (wxPayRefund != null) {// 已创建过退款记录
			if (WxPayConstants.RefundStatus.SUCCESS.equals(wxPayRefund.getRefundStatus())) {// 已成功退款
				return;
			}
			wxPayRefundId = wxPayRefund.getId();
		}
    	
		WxPay wxPay = wxPayMapper.selectByOutTradeNo(outTradeNo);
		
		// 公共参数
		WxPayConfig wxPayConfig = new WxPayConfig();
        wxPayConfig.setAppId(wxPay.getAppid());
        wxPayConfig.setMchId(wxPay.getMchid());
        WxPayProperties.MchConfig mchConfig = wxPayConfiguration.getMchConfig(wxPay.getMchid());
        wxPayConfig.setMchKey(mchConfig.getMchKey());
        wxPayConfig.setKeyPath(mchConfig.getKeyPath());// 退款时用到证书
        
		// 私有参数
		WxPayRefundRequest request = WxPayRefundRequest.newBuilder().build();
        request.setNonceStr(RandomStringUtils.randomAlphanumeric(16));
        request.setOutTradeNo(wxPay.getOutTradeNo());
        request.setOutRefundNo(outRefundNo);
        request.setTotalFee(wxPay.getTotalFee());
        request.setRefundFee(refundAmount.multiply(new BigDecimal(100)).intValue());
        request.setNotifyUrl(domain + REFUND_CALLBACK_URL);
        
        try {
			WxPayService wxPayService = new WxPayServiceImpl();
	        wxPayService.setConfig(wxPayConfig);
	        
        	WxPayRefundResult refundResult = wxPayService.refund(request);

			refundResult2WxPayRefund(wxPayRefundId, wxPayConfig, request, refundResult, null);
			
			String returnCode = refundResult.getReturnCode();
			if (!Objects.equal(returnCode, WxPayConstants.ResultCode.SUCCESS)) {
				throw new BusinessException(refundResult.getReturnMsg());
			}
			
			String resultCode = refundResult.getResultCode();
			if (!Objects.equal(resultCode, WxPayConstants.ResultCode.SUCCESS)) {
				throw new BusinessException(refundResult.getErrCodeDes());
			}
		} catch (WxPayException e) {
			// 退款异常
			log.error("WxPayRefund error", e);
			WxPayRefundResult result = null;
			if (StringUtils.isNotBlank(e.getXmlString())) {
				result = BaseWxPayResult.fromXML(e.getXmlString(), WxPayRefundResult.class);
			} else {
				result = new WxPayRefundResult();
				result.setErrCode(WxPayErrorCode.Refund.PARAM_ERROR);
				result.setErrCodeDes(e.getCustomErrorMsg());
			}
			refundResult2WxPayRefund(wxPayRefundId, wxPayConfig, request, result, "请求异常,logid:" + MdcUtil.get());
			throw new BusinessException(Optional.ofNullable(e.getErrCodeDes()).orElse(e.getReturnMsg()));
		}
	}

	private void refundResult2WxPayRefund(Integer wxPayRefundId, WxPayConfig wxPayConfig, WxPayRefundRequest request,
			WxPayRefundResult result, String remark) {
		
		// 保存微信支付退款数据
    	WxPayRefund wxPayRefund = new WxPayRefund()
				/* 退款参数 */
    			.setAppid              (wxPayConfig.getAppId())
				.setMchid              (wxPayConfig.getMchId())
				.setNonceStr           (request.getNonceStr())
				.setSign           	(request.getSign())
				.setOutTradeNo         (request.getOutTradeNo())
				.setOutRefundNo         (request.getOutRefundNo())
				.setTotalFee           (request.getTotalFee())
				.setRefundFee           (request.getRefundFee())
				;
    	
		if (result != null) {
			/* 退款结果 */
			wxPayRefund.setReturnCode(result.getReturnCode());
			wxPayRefund.setReturnMsg(result.getReturnMsg());
			wxPayRefund.setResultCode(result.getResultCode());
			wxPayRefund.setErrCode(result.getErrCode());
			wxPayRefund.setErrCodeDes(result.getErrCodeDes());
			wxPayRefund.setRefundId(result.getRefundId());
			wxPayRefund.setCashFee(result.getCashFee());
		}
		wxPayRefund.setRemark(remark);
		
		if (wxPayRefundId == null) {
			wxPayRefundMapper.insert(wxPayRefund);
		} else {
			wxPayRefund.setId(wxPayRefundId);
			wxPayRefundMapper.updateById(wxPayRefund);
		}
	}

	@Override
	protected void requestPayCloseOrder(String outTradeNo) {
		WxPay wxPay = wxPayMapper.selectByOutTradeNo(outTradeNo);
		if (wxPay == null) {
			throw new BusinessException("未找到订单，不用关闭");
		}
		// 未成功下过单，不用关闭
		if (!WxPayConstants.ResultCode.SUCCESS.equals(wxPay.getResultCode())) {
			throw new BusinessException("未成功下过单，不用关闭");
		}

		// 公共参数
		WxPayConfig wxPayConfig = new WxPayConfig();
		wxPayConfig.setAppId(wxPay.getAppid());
		wxPayConfig.setMchId(wxPay.getMchid());
		WxPayProperties.MchConfig mchConfig = wxPayConfiguration.getMchConfig(wxPay.getMchid());
		wxPayConfig.setMchKey(mchConfig.getMchKey());
		
		try {
			WxPayService wxPayService = new WxPayServiceImpl();
			wxPayService.setConfig(wxPayConfig);

			// 私有参数
			WxPayOrderCloseRequest orderCloseRequest = new WxPayOrderCloseRequest();
			orderCloseRequest.setOutTradeNo(outTradeNo);
			orderCloseRequest.setNonceStr(RandomStringUtils.randomAlphanumeric(16));

			// 调用微信关闭订单接口
			WxPayOrderCloseResult orderCloseResult = wxPayService.closeOrder(orderCloseRequest);
			String returnCode = orderCloseResult.getReturnCode();
			if (!Objects.equal(returnCode, WxPayConstants.ResultCode.SUCCESS)) {
				throw new BusinessException(orderCloseResult.getReturnMsg());
			}

			String resultCode = orderCloseResult.getResultCode();
			if (!Objects.equal(resultCode, WxPayConstants.ResultCode.SUCCESS)) {
				throw new BusinessException(orderCloseResult.getErrCodeDes());
			}

		} catch (WxPayException e) {
			log.error("Wx pay close order error.", e);
			throw new BusinessException(Optional.ofNullable(e.getErrCodeDes()).orElse(e.getReturnMsg()));
		}
	}
}
