package com.company.order.pay.wx;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.company.common.exception.BusinessException;
import com.company.framework.util.Utils;
import com.company.framework.context.SpringContextUtil;
import com.company.order.api.response.PayOrderQueryResp;
import com.company.order.api.response.PayRefundQueryResp;
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
import com.github.binarywang.wxpay.bean.request.WxPayRefundQueryRequest;
import com.github.binarywang.wxpay.bean.request.WxPayRefundRequest;
import com.github.binarywang.wxpay.bean.request.WxPayUnifiedOrderRequest;
import com.github.binarywang.wxpay.bean.result.BaseWxPayResult;
import com.github.binarywang.wxpay.bean.result.WxPayOrderCloseResult;
import com.github.binarywang.wxpay.bean.result.WxPayOrderQueryResult;
import com.github.binarywang.wxpay.bean.result.WxPayRefundQueryResult;
import com.github.binarywang.wxpay.bean.result.WxPayRefundQueryResult.RefundRecord;
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

	private static final String PAY_CALLBACK_URL = "/notify/wxPay";
	private static final String REFUND_CALLBACK_URL = "/notify/wxPayRefund";

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

		String appid = payParams.getAppid();
		PayConfig payConfig = wxPayConfiguration.getPayConfig(appid);

		String tradeType = payConfig.getTradeType();
		Integer wxPayId = null;
		String remark = null;
		if (wxPay != null) {// 已下过单
			if (!StringUtils.isAllBlank(wxPay.getPrepayId(), wxPay.getMwebUrl(), wxPay.getCodeUrl())) {// 已成功下过单
				OrderResultTransfer orderResultTransfer = SpringContextUtil
						.getBean(OrderResultTransfer.BEAN_NAME_PREFIX + tradeType, OrderResultTransfer.class);
				Object payInfo = orderResultTransfer.toPayInfo(wxPay.getAppid(), wxPay.getMchid(),
						wxPay.getPrepayId(), wxPay.getCodeUrl(), wxPay.getMwebUrl());
				return payInfo;
			}
			wxPayId = wxPay.getId();
			remark = wxPay.getRemark();
		}

		// 公共参数
		WxPayConfig wxPayConfig = new WxPayConfig();
		wxPayConfig.setAppId(appid);

		String mchid = payParams.getMchid();
		if (StringUtils.isBlank(mchid)) {
			mchid = payConfig.getMchId();
		}
		wxPayConfig.setMchId(mchid);
		WxPayProperties.MchConfig mchConfig = wxPayConfiguration.getMchConfig(mchid);
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

			remark = Utils.rightRemark(remark, resultMsg(unifiedOrderResult));
			requestResult2WxPay(wxPayId, appid, mchid, request, unifiedOrderResult, remark);

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

			remark = Utils.rightRemark(remark, resultMsg(result));
			remark = Utils.rightRemark(remark, "请求异常");
			requestResult2WxPay(wxPayId, appid, mchid, request, result, remark);

			throw new BusinessException(StringUtils.getIfBlank(e.getErrCodeDes(), () -> e.getReturnMsg()));
		}
	}

	private void requestResult2WxPay(Integer wxPayId, String appid, String mchid, WxPayUnifiedOrderRequest request,
			WxPayUnifiedOrderResult result, String remark) {
		// 保存微信支付数据
		WxPay wxPay = new WxPay()
				/* 支付参数 */
				.setAppid              (appid)
				.setMchid              (mchid)
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
	public PayOrderQueryResp orderQuery(String outTradeNo) {
		PayOrderQueryResp resp = new PayOrderQueryResp();
		WxPay wxPay = wxPayMapper.selectByOutTradeNo(outTradeNo);
		if (wxPay == null) {
			resp.setResult(false);
			resp.setMessage("订单不存在");
			return resp;
		}

		if (WxPayConstants.WxpayTradeStatus.SUCCESS.equals(wxPay.getTradeState())
				|| WxPayConstants.WxpayTradeStatus.CLOSED.equals(wxPay.getTradeState())) {
			// 出结果了
			resp.setResult(true);

			boolean paySuccess = Objects.equal(wxPay.getTradeState(), WxPayConstants.WxpayTradeStatus.SUCCESS);
			resp.setPaySuccess(paySuccess);

			if (paySuccess) {
				resp.setPayAmount(
						new BigDecimal(wxPay.getTotalFee()).divide(new BigDecimal(100), 2, BigDecimal.ROUND_HALF_UP));
				LocalDateTime time = LocalDateTime.now();
				if (StringUtils.isNotBlank(wxPay.getTimeEnd())) {
					time = LocalDateTime.parse(wxPay.getTimeEnd(), DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
				}
				resp.setPayTime(time);
				resp.setMerchantNo(wxPay.getMchid());
				resp.setTradeNo(wxPay.getTransactionId());
			}
			return resp;
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
				resp.setResult(false);
				resp.setMessage(orderQueryResult.getReturnMsg());
				return resp;
			}

			String resultCode = orderQueryResult.getResultCode();
			if (!Objects.equal(resultCode, WxPayConstants.ResultCode.SUCCESS)) {
				resp.setResult(false);
				resp.setMessage(orderQueryResult.getErrCodeDes());
				return resp;
			}

			// 保存查询结果数据
			String remark = Utils.rightRemark(wxPay.getRemark(), "tradeState:" + orderQueryResult.getTradeState());
			queryResult2WxPay(outTradeNo, orderQueryResult, remark);

			if (!(WxPayConstants.WxpayTradeStatus.SUCCESS.equals(orderQueryResult.getTradeState())
					|| WxPayConstants.WxpayTradeStatus.CLOSED.equals(orderQueryResult.getTradeState()))) {
				// 未出结果
				resp.setResult(false);
				resp.setMessage("未出结果");
				return resp;
			}

			resp.setResult(true);

			boolean paySuccess = Objects.equal(orderQueryResult.getTradeState(),
					WxPayConstants.WxpayTradeStatus.SUCCESS);
			resp.setPaySuccess(paySuccess);

			if (paySuccess) {
				resp.setPayAmount(
						new BigDecimal(wxPay.getTotalFee()).divide(new BigDecimal(100), 2, BigDecimal.ROUND_HALF_UP));

				LocalDateTime time = LocalDateTime.now();
				if (StringUtils.isNotBlank(orderQueryResult.getTimeEnd())) {
					time = LocalDateTime.parse(orderQueryResult.getTimeEnd(),
							DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
				}
				resp.setPayTime(time);
				resp.setMerchantNo(wxPay.getMchid());
				resp.setTradeNo(orderQueryResult.getTransactionId());
			}

			return resp;
		} catch (WxPayException e) {
			// 查询异常
			log.error("WxPay orderQuery error", e);
			if (Objects.equal(e.getErrCode(), WxPayErrorCode.OrderClose.ORDER_NOT_EXIST)) {
				resp.setResult(true);
				resp.setMessage(e.getErrCodeDes());
				resp.setPaySuccess(false);
				return resp;
			}
			throw new BusinessException(StringUtils.getIfBlank(e.getErrCodeDes(), () -> e.getReturnMsg()));
		}
	}

	private boolean queryResult2WxPay(String outTradeNo, WxPayOrderQueryResult result, String remark) {
		// 保存微信查询结果数据
		WxPay wxPay4Update = new WxPay().setTradeState(result.getTradeState())
				.setTransactionId(result.getTransactionId()).setTimeEnd(result.getTimeEnd()).setRemark(remark);

		UpdateWrapper<WxPay> wrapper = new UpdateWrapper<WxPay>();
		wrapper.eq("out_trade_no", outTradeNo);
		wrapper.and(i -> i.isNull("trade_status")
				.or(i2 -> i2.ne("trade_status", WxPayConstants.WxpayTradeStatus.SUCCESS)));
		Integer affect = wxPayMapper.update(wxPay4Update, wrapper);
		return affect > 0;
	}

	@Override
	public void requestRefund(String outTradeNo, String outRefundNo, BigDecimal refundAmount) {
		WxPayRefund wxPayRefund = wxPayRefundMapper.selectByOutRefundNo(outRefundNo);
		Integer wxPayRefundId = null;
		String remark = null;
		if (wxPayRefund != null) {// 已创建过退款记录
			if (WxPayConstants.RefundStatus.SUCCESS.equals(wxPayRefund.getRefundStatus())) {// 已成功退款
				return;
			}
			wxPayRefundId = wxPayRefund.getId();
			remark = wxPayRefund.getRemark();
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

			remark = Utils.rightRemark(remark, resultMsg(refundResult));
			refundResult2WxPayRefund(wxPayRefundId, wxPayConfig, request, refundResult, remark);

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

			remark = Utils.rightRemark(remark, resultMsg(result));
			remark = Utils.rightRemark(remark, "请求异常");
			refundResult2WxPayRefund(wxPayRefundId, wxPayConfig, request, result, remark);

			throw new BusinessException(StringUtils.getIfBlank(e.getErrCodeDes(), () -> e.getReturnMsg()));
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
			// 未找到订单，不用关闭（可认为是关闭成功）
			return;
		}

		if (StringUtils.isAllBlank(wxPay.getPrepayId(), wxPay.getMwebUrl(), wxPay.getCodeUrl())) {// 未成功下过单
			// 未成功下过单，不用关闭（可认为是关闭成功）
			return;
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
			if (Objects.equal(resultCode, "ORDERCLOSED")) {// 订单已关闭，无需继续调用
				return;
			}
			if (!Objects.equal(resultCode, WxPayConstants.ResultCode.SUCCESS)) {
				throw new BusinessException(orderCloseResult.getErrCodeDes());
			}

		} catch (WxPayException e) {
			log.error("Wx pay close order error.", e);
			throw new BusinessException(StringUtils.getIfBlank(e.getErrCodeDes(), () -> e.getReturnMsg()));
		}
	}

	@Override
	public PayRefundQueryResp refundQuery(String outRefundNo) {
		PayRefundQueryResp resp = new PayRefundQueryResp();
		WxPayRefund wxPayRefund = wxPayRefundMapper.selectByOutRefundNo(outRefundNo);
		if (wxPayRefund == null) {
			resp.setResult(false);
			resp.setMessage("订单不存在");
			return resp;
		}
		if (WxPayConstants.RefundStatus.SUCCESS.equals(wxPayRefund.getRefundStatus())
				|| WxPayConstants.RefundStatus.REFUND_CLOSE.equals(wxPayRefund.getRefundStatus())) {
			// 已有退款结果
			resp.setResult(true);

			boolean refundSuccess = Objects.equal(wxPayRefund.getRefundStatus(), WxPayConstants.RefundStatus.SUCCESS);
			resp.setRefundSuccess(refundSuccess);

			if (refundSuccess) {
				resp.setRefundAmount(new BigDecimal(wxPayRefund.getRefundFee()).divide(new BigDecimal(100), 2,
						BigDecimal.ROUND_HALF_UP));
				resp.setMerchantNo(wxPayRefund.getMchid());
				resp.setTradeNo(wxPayRefund.getRefundId());
			}
			return resp;
		}

		// 公共参数
		WxPayConfig wxPayConfig = new WxPayConfig();
		wxPayConfig.setAppId(wxPayRefund.getAppid());
		wxPayConfig.setMchId(wxPayRefund.getMchid());
		WxPayProperties.MchConfig mchConfig = wxPayConfiguration.getMchConfig(wxPayRefund.getMchid());
		wxPayConfig.setMchKey(mchConfig.getMchKey());

		try {
			WxPayService wxPayService = new WxPayServiceImpl();
			wxPayService.setConfig(wxPayConfig);

			// 私有参数
			WxPayRefundQueryRequest request = WxPayRefundQueryRequest.newBuilder().build();
			request.setOutRefundNo(outRefundNo);
			request.setNonceStr(RandomStringUtils.randomAlphanumeric(16));

			WxPayRefundQueryResult refundQueryResult = wxPayService.refundQuery(request);

			String returnCode = refundQueryResult.getReturnCode();
			if (!Objects.equal(returnCode, WxPayConstants.ResultCode.SUCCESS)) {
				resp.setResult(false);
				resp.setMessage(refundQueryResult.getReturnMsg());
				return resp;
			}

			String resultCode = refundQueryResult.getResultCode();
			if (!Objects.equal(resultCode, WxPayConstants.ResultCode.SUCCESS)) {
				resp.setResult(false);
				resp.setMessage(refundQueryResult.getErrCodeDes());
				return resp;
			}

			List<RefundRecord> refundRecords = refundQueryResult.getRefundRecords();
			if (CollectionUtils.isEmpty(refundRecords)) {
				resp.setResult(false);
				resp.setMessage("未查询到退款订单");
				return resp;
			}

			Map<String, RefundRecord> outRefundNoRefundRecordMap = refundRecords.stream()
					.collect(Collectors.toMap(RefundRecord::getOutRefundNo, a -> a));

			RefundRecord refundRecord = outRefundNoRefundRecordMap.get(outRefundNo);
			if (refundRecord == null) {
				resp.setResult(false);
				resp.setMessage("未查询到退款订单");
				return resp;
			}

			String refundStatus = refundRecord.getRefundStatus();

			// 保存查询结果数据
			String remark = Utils.rightRemark(wxPayRefund.getRemark(), "refundStatus:" + refundStatus);
			refundQueryResult2WxPay(outRefundNo, refundStatus, remark);

			if (!(WxPayConstants.RefundStatus.SUCCESS.equals(refundStatus)
					|| WxPayConstants.RefundStatus.REFUND_CLOSE.equals(refundStatus))) {
				// 未出结果
				resp.setResult(false);
				resp.setMessage("未出结果");
				return resp;
			}

			resp.setResult(true);

			boolean refundSuccess = Objects.equal(refundRecord.getRefundStatus(), WxPayConstants.RefundStatus.SUCCESS);
			resp.setRefundSuccess(refundSuccess);

			if (refundSuccess) {
				resp.setRefundAmount(new BigDecimal(wxPayRefund.getTotalFee()).divide(new BigDecimal(100), 2,
						BigDecimal.ROUND_HALF_UP));
				resp.setMerchantNo(wxPayRefund.getMchid());
				resp.setTradeNo(refundRecord.getRefundId());
			}

			return resp;
		} catch (WxPayException e) {
			// 查询异常
			log.error("WxPay refundQuery error", e);
			if (Objects.equal(e.getErrCode(), WxPayErrorCode.RefundQuery.REFUNDNOTEXIST)) {
				resp.setResult(true);
				resp.setMessage(e.getErrCodeDes());
				resp.setRefundSuccess(false);
				return resp;
			}
			throw new BusinessException(StringUtils.getIfBlank(e.getErrCodeDes(), () -> e.getReturnMsg()));
		}
	}

	private boolean refundQueryResult2WxPay(String outRefundNo, String refundStatus, String remark) {
		// 保存微信查询结果数据
		WxPayRefund wxPay4Update = new WxPayRefund().setRefundStatus(refundStatus).setRemark(remark);

		UpdateWrapper<WxPayRefund> wrapper = new UpdateWrapper<WxPayRefund>();
		wrapper.eq("out_refund_no", outRefundNo);
		wrapper.and(i -> i.isNull("refund_status")
				.or(i2 -> i2.ne("refund_status", WxPayConstants.RefundStatus.SUCCESS)));
		int affect = wxPayRefundMapper.update(wxPay4Update, wrapper);
		return affect > 0;
	}

	private String resultMsg(BaseWxPayResult result) {
		String remark = "";
		String returnCode = result.getReturnCode();
		if (StringUtils.isNotBlank(returnCode)) {
			remark = Utils.rightRemark(remark, returnCode + "(" + result.getReturnMsg() + ")");
		}
		String resultCode = result.getResultCode();
		if (StringUtils.isNotBlank(resultCode)) {
			remark = Utils.rightRemark(remark, resultCode);
		}
		String errCode = result.getErrCode();
		if (StringUtils.isNotBlank(errCode)) {
			remark = Utils.rightRemark(remark, errCode + "(" + result.getErrCodeDes() + ")");
		}
		return remark;
	}

}
