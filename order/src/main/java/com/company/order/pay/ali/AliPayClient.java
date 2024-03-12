package com.company.order.pay.ali;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradeAppPayModel;
import com.alipay.api.domain.AlipayTradeRefundModel;
import com.alipay.api.request.AlipayTradeAppPayRequest;
import com.alipay.api.request.AlipayTradeCloseRequest;
import com.alipay.api.request.AlipayTradeQueryRequest;
import com.alipay.api.request.AlipayTradeRefundRequest;
import com.alipay.api.response.AlipayTradeAppPayResponse;
import com.alipay.api.response.AlipayTradeCloseResponse;
import com.alipay.api.response.AlipayTradeQueryResponse;
import com.alipay.api.response.AlipayTradeRefundResponse;
import com.company.common.exception.BusinessException;
import com.company.common.util.JsonUtil;
import com.company.common.util.MdcUtil;
import com.company.common.util.Utils;
import com.company.framework.context.SpringContextUtil;
import com.company.order.api.response.PayTradeStateResp;
import com.company.order.entity.AliPay;
import com.company.order.entity.AliPayRefund;
import com.company.order.mapper.AliPayMapper;
import com.company.order.mapper.AliPayRefundMapper;
import com.company.order.pay.PayFactory;
import com.company.order.pay.ali.config.AliPayConfiguration;
import com.company.order.pay.ali.config.AliPayProperties.PayConfig;
import com.company.order.pay.ali.mock.NotifyMock;
import com.company.order.pay.core.BasePayClient;
import com.company.order.pay.dto.PayParams;

import lombok.extern.slf4j.Slf4j;

/**
 * 支付宝APP支付
 * 
 * <pre>
 * 支付宝钱包支付：用户点击支付，唤起支付宝收银台后， 输入正确完整的支付密码后订单创建。
 * 所以调用支付宝的sdk并不会在支付宝创建订单，需要支付完后才会创建订单
 * </pre>
 */
@Slf4j
@Component(PayFactory.ALI_PAYCLIENT)
public class AliPayClient extends BasePayClient {

	private static final String PAY_URL = "https://openapi.alipay.com/gateway.do";
	private static final String PAY_CALLBACK_URL = "/server/callback/ali";
	
	@Autowired
	private AliPayConfiguration aliPayConfiguration;
	@Autowired
	private AliPayMapper aliPayMapper;
	@Autowired
	private AliPayRefundMapper aliPayRefundMapper;
	@Autowired
	private ThreadPoolTaskExecutor executor;
	
	@Value("${template.domain}")
	private String domain;
	
	@Value("${timeExpire.seconds:180}")
	private Integer timeExpireSeconds;
	
	@Value("${template.mock.wxnotify:false}")
	private Boolean mockNotify;
	
	@Override
    protected Object requestPayInfo(PayParams payParams) {
		AliPay aliPay = aliPayMapper.selectByOutTradeNo(payParams.getOutTradeNo());
		Integer aliPayId = null;
		String remark = null;
		if (aliPay != null) {// 已下过单
			/*
			 * 缩短支付宝订单有效时间，预防pay body被利用
			 * 
			if (StringUtils.isNotBlank(aliPay.getPayBody())) {// 已成功下过单
				return aliPay.getPayBody();
			}
			*/
			aliPayId = aliPay.getId();
			remark = aliPay.getRemark();
		}
		
    	String notifyUrl = domain + PAY_CALLBACK_URL;
    	
		/**
		 * 官方文档：https://opendocs.alipay.com/pre-open/02e7gd
		 */
    	AlipayTradeAppPayRequest request = new AlipayTradeAppPayRequest();
        AlipayTradeAppPayModel model = new AlipayTradeAppPayModel();
        model.setOutTradeNo(payParams.getOutTradeNo());
		model.setSubject(payParams.getBody());
        model.setTotalAmount(payParams.getAmount().toString());
        
        // 缩短支付宝订单有效时间
        String timeExpire = DateFormatUtils.format(DateUtils.addSeconds(new Date(), timeExpireSeconds),
				"yyyy-MM-dd HH:mm:ss");
		model.setTimeExpire(timeExpire);
		
        request.setBizModel(model);
        request.setNotifyUrl(notifyUrl);
        
        PayConfig payConfig = aliPayConfiguration.getPayConfig(payParams.getAppid());
		try {
			AlipayClient alipayClient = new DefaultAlipayClient(PAY_URL,
					payConfig.getAppId(),
					payConfig.getPrivateKey(),
	                AliConstants.FORMAT,
	                AliConstants.CHARSET,
	                payConfig.getPubKey(),
	                AliConstants.SIGNTYPE);
		    
            AlipayTradeAppPayResponse response = alipayClient.sdkExecute(request);
            remark = Utils.rightRemark(remark, timeExpire);
			requestResult2AliPay(aliPayId, payParams, request, response, remark);
            
			if (!response.isSuccess()) {
				throw new BusinessException(StringUtils.getIfBlank(response.getSubMsg(), () -> response.getMsg()));
			}
			
			if (mockNotify && !SpringContextUtil.isProduceProfile()) {
				executor.submit(() -> {
					try {
						Thread.sleep(10000);// 10s后回调
					} catch (InterruptedException e) {
						log.error("sleep error", e);
					}
					log.info("模拟微信支付回调入口");
					NotifyMock.payNotify(payConfig, request);
				});
			}
			
			// 可以直接返给客户端，无需再做处理
			return response.getBody();// alipay_sdk=alipay-sdk-java-3.4.49.ALL&app_id=2022001116633522&biz_content=%7B%22body%22%3A%22testorder%22%2C%22out_trade_no%22%3A%22205546307409489920%22%2C%22subject%22%3A%22testorder-subject%22%2C%22total_amount%22%3A%220.01%22%7D&charset=UTF-8&format=json&method=alipay.trade.app.pay&notify_url=https%3A%2F%2Fapi.domain.com%2Fapi%2Fcallback%2Falipay%2F&sign=hdZMVVmslawvRMDKwIX%2Bcj%2BeSKHrIn7Zna5ngQt0rYgtPladQmAmdOILsNeIMCFv7ZFRVLIvjWNwsYiOxKxYdbo%2FRnR6JMbK7PSJg%2BCAjTZQYR5abcU%2Fq1eL0cjuwbtHwAC9qLBF7B95XYHeJkwwxZgK903Eb9vRyJCw6fTl7egnL3ceRphqC315gajxSzf0UkefxrWjIu55ov8zYd1wdZQ64jsJjS9y0Bh1iW5qWBXNq0lOU8VXLdg2vXUycOJs9wLP7Uv2ob%2FKmcy5DxfrPh4A9DNllwwKKwhVqPbGqQgwqArQh2hjy0FyYDQ9%2B%2FwbylG97Au6lqHlbP4GtMQVEw%3D%3D&sign_type=RSA2&timestamp=2020-12-30+09%3A26%3A51&version=1.0
        } catch (AlipayApiException e) {
        	// 支付异常
			log.error("AliPay error", e);
			remark = Utils.rightRemark(remark, timeExpire);
			remark = Utils.rightRemark(remark, "请求异常,logid:" + MdcUtil.get());
			requestResult2AliPay(aliPayId, payParams, request, null, remark);
			throw new BusinessException(e.getErrMsg());
        } catch (Exception e) {
        	// 支付异常
			log.error("AliPay error", e);
			remark = Utils.rightRemark(remark, timeExpire);
			remark = Utils.rightRemark(remark, "请求异常,logid:" + MdcUtil.get());
			requestResult2AliPay(aliPayId, payParams, request, null, remark);
			throw new BusinessException(e.getMessage());
        }
    }

	private void requestResult2AliPay(Integer aliPayId, PayParams payParams,
			AlipayTradeAppPayRequest request, AlipayTradeAppPayResponse result, String remark) {
		// 保存支付宝支付数据
    	AliPay aliPay = new AliPay()
				/* 支付参数 */
				.setAppid              (payParams.getAppid())
				.setNotifyUrl          (request.getNotifyUrl())
				.setOutTradeNo         (payParams.getOutTradeNo())
				.setSubject               (payParams.getBody())
				.setTotalAmount           (payParams.getAmount())
				;
    	
		if (result != null) {
			/* 支付信息 */
			if (result.isSuccess()) {
				aliPay.setPayBody(result.getBody());
			}
		}
		aliPay.setRemark(remark);

		if (aliPayId == null) {
			aliPayMapper.insert(aliPay);
		} else {
			aliPay.setId(aliPayId);
			aliPayMapper.updateById(aliPay);
		}
	}

	@Override
	public PayTradeStateResp queryTradeState(String outTradeNo) {
		PayTradeStateResp payTradeStateResp = new PayTradeStateResp();
		AliPay aliPay = aliPayMapper.selectByOutTradeNo(outTradeNo);
		if (aliPay != null && aliPay.getTradeStatus() != null) {
			// 回调结果
			payTradeStateResp.setResult(true);
			payTradeStateResp.setPaySuccess(Objects.equals(aliPay.getTradeStatus(), AliConstants.TRADE_SUCCESS));
			return payTradeStateResp;
		}
		AlipayTradeQueryRequest request = new AlipayTradeQueryRequest();
		AlipayTradeAppPayModel model = new AlipayTradeAppPayModel();
		model.setOutTradeNo(aliPay.getOutTradeNo());
		request.setBizModel(model);
		
		try {
			PayConfig payConfig = aliPayConfiguration.getPayConfig(aliPay.getAppid());
			AlipayClient alipayClient = new DefaultAlipayClient(PAY_URL,
					aliPay.getAppid(),
					payConfig.getPrivateKey(),
	                AliConstants.FORMAT,
	                AliConstants.CHARSET,
	                payConfig.getPubKey(),
	                AliConstants.SIGNTYPE);
			
            AlipayTradeQueryResponse response = alipayClient.sdkExecute(request);
            
			if (!response.isSuccess()) {
				payTradeStateResp.setResult(false);
				payTradeStateResp.setMessage(StringUtils.getIfBlank(response.getSubMsg(), () -> response.getMsg()));
				return payTradeStateResp;
			}
			
			payTradeStateResp.setResult(true);
			payTradeStateResp.setMessage(StringUtils.defaultIfBlank(response.getSubMsg(), response.getMsg()));
			payTradeStateResp.setPaySuccess(Objects.equals(response.getTradeStatus(), AliConstants.TRADE_SUCCESS));
			return payTradeStateResp;
        } catch (AlipayApiException e) {
        	// 支付异常
			log.error("AliPay tradeStateSuccess error", e);
			throw new BusinessException(e.getErrMsg());
        }
	}
	
	@Override
	public void requestRefund(String outTradeNo, String outRefundNo, BigDecimal refundAmount) {
		AliPayRefund aliPayRefund = aliPayRefundMapper.selectByOutRequestNo(outRefundNo);
		Integer aliPayRefundId = null;
		if (aliPayRefund != null) {// 已创建过退款记录
			if (StringUtils.isNotBlank(aliPayRefund.getTradeStatus())) {// 已成功退款
				// 部分退款：TRADE_SUCCESS，全额退款：TRADE_CLOSED
				return;
			}
			aliPayRefundId = aliPayRefund.getId();
		}
		
		AliPay aliPay = aliPayMapper.selectByOutTradeNo(outTradeNo);
		
		AlipayTradeRefundRequest request = new AlipayTradeRefundRequest();
        AlipayTradeRefundModel model = new AlipayTradeRefundModel();
        model.setOutTradeNo(outTradeNo);
        model.setRefundAmount(refundAmount.toString());
		model.setRefundReason("正常退款");
        model.setOutRequestNo(outRefundNo);//此参数为部分退款时用到，全部退款也不影响
        request.setBizModel(model);
		// 支付宝退款通知url不支持动态设置，而是直接把支付时设置的通知url作为退款通知url
		// request.setNotifyUrl(notifyUrl);

		try {
			PayConfig payConfig = aliPayConfiguration.getPayConfig(aliPay.getAppid());
			AlipayClient alipayClient = new DefaultAlipayClient(PAY_URL,
					aliPay.getAppid(),
					payConfig.getPrivateKey(),
	                AliConstants.FORMAT,
	                AliConstants.CHARSET,
	                payConfig.getPubKey(),
	                AliConstants.SIGNTYPE);
        	
        	refundResult2AliPayRefund(aliPayRefundId, aliPay, outRefundNo, refundAmount, null);
			AlipayTradeRefundResponse response = alipayClient.execute(request);
			log.info("refund response:{}", JsonUtil.toJsonString(response));
			updateRefundResult(outRefundNo, response);

			if (!response.isSuccess()) {
				throw new BusinessException(StringUtils.getIfBlank(response.getSubMsg(), () -> response.getMsg()));
			}
		} catch (AlipayApiException e) {
			// 退款异常
			log.error("AliPayRefund error", e);
			String remark = Utils.rightRemark(aliPayRefund.getRemark(), "请求异常,logid:" + MdcUtil.get());
			refundResult2AliPayRefund(aliPayRefundId, aliPay, outRefundNo, refundAmount, remark);
			throw new BusinessException(e.getErrMsg());
		}
	}

	private void updateRefundResult(String outRefundNo, AlipayTradeRefundResponse response) {
		AliPayRefund aliPayRefunddb = aliPayRefundMapper.selectByOutRequestNo(outRefundNo);
		// 保存支付宝支付退款数据
		AliPayRefund aliPayRefund = new AliPayRefund();
		if (response != null) {
			// 支付退款结果
			if (!response.isSuccess()) {
				String msg = StringUtils.getIfBlank(response.getSubMsg(), () -> response.getMsg());
				String remark = Utils.rightRemark(aliPayRefunddb.getRemark(), msg);
				aliPayRefund.setRemark(remark);
			}
		}
		aliPayRefund.setUpdateTime(LocalDateTime.now());
		aliPayRefund.setId(aliPayRefunddb.getId());
		aliPayRefundMapper.updateById(aliPayRefund);

	}
	
	private void refundResult2AliPayRefund(Integer aliPayRefundId, AliPay aliPay, String outRefundNo,
			BigDecimal refundAmount, String remark) {
		// 保存支付宝支付退款数据
		AliPayRefund aliPayRefund = new AliPayRefund()
				/* 退款参数 */
				.setAppid(aliPay.getAppid())
				.setOutTradeNo(aliPay.getOutTradeNo())
				.setOutRequestNo(outRefundNo)
				.setRefundAmount(refundAmount);

		aliPayRefund.setRemark(remark);
		
		if (aliPayRefundId == null) {
			aliPayRefundMapper.insert(aliPayRefund);
		} else {
			aliPayRefund.setId(aliPayRefundId);
			aliPayRefundMapper.updateById(aliPayRefund);
		}
	}

	@Override
	protected void requestPayCloseOrder(String outTradeNo) {
		AliPay aliPay = aliPayMapper.selectByOutTradeNo(outTradeNo);

		if (aliPay == null) {
			// 未找到订单，不用关闭（可认为是关闭成功）
			return;
		}

		/**
		 * 官方文档：https://opendocs.alipay.com/pre-open/02e5yz?pathHash=e5f631b6
		 */
		AlipayTradeCloseRequest closeRequest = new AlipayTradeCloseRequest();
		Map<String, String> cancelJsonObject = new HashMap<>();
		cancelJsonObject.put("out_trade_no", outTradeNo);
		closeRequest.setBizContent(JsonUtil.toJsonString(cancelJsonObject));

		try {
			PayConfig payConfig = aliPayConfiguration.getPayConfig(aliPay.getAppid());
			AlipayClient alipayClient = new DefaultAlipayClient(PAY_URL,
					aliPay.getAppid(),
					payConfig.getPrivateKey(),
	                AliConstants.FORMAT,
	                AliConstants.CHARSET,
	                payConfig.getPubKey(),
	                AliConstants.SIGNTYPE);

			AlipayTradeCloseResponse response = alipayClient.execute(closeRequest);
			log.info("requestPayCloseOrder is {}", JsonUtil.toJsonString(response));
			// 未支付的订单支付宝不会创建订单，所以响应‘交易不存在’，保留调用该接口，或许其他支付业务会用到
			String subCode = response.getSubCode();
			if ("ACQ.TRADE_NOT_EXIST".equals(subCode)) {// 交易不存在（可认为是关闭成功）
				return;
			}
			if (!response.isSuccess()) {
				throw new BusinessException(StringUtils.getIfBlank(response.getSubMsg(), () -> response.getMsg()));
			}
		} catch (AlipayApiException e) {
			// 关闭异常
			log.error("AliPayCloseOrder error", e);
			throw new BusinessException(e.getErrMsg());
		}
	}
}
