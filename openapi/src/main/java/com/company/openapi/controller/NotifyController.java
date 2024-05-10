package com.company.openapi.controller;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.company.openapi.annotation.NoSign;
import com.company.order.api.feign.AliActivityNotifyFeign;
import com.company.order.api.feign.AliNotifyFeign;
import com.company.order.api.feign.WxNotifyFeign;
import com.company.order.api.response.SpiOrderSendNotifyResp;

/**
 * 接收第三方回调请求
 */
@NoSign // 第三方回调请求有自己的验签，所以这里跳过验签，由后续业务进行验签
@RestController
@RequestMapping(value = "/notify")
public class NotifyController {

	@Autowired
	private WxNotifyFeign wxNotifyFeign;
	@Autowired
	private AliNotifyFeign aliNotifyFeign;
	@Autowired
	private AliActivityNotifyFeign aliActivityNotifyFeign;

	@PostMapping(value = "/wxPay", produces = MediaType.APPLICATION_XML_VALUE)
	public String wxPay(@RequestBody String xmlString) {
		return wxNotifyFeign.wxPayNotify(xmlString).dataOrThrow();
	}

	@PostMapping(value = "/wxPayRefund", produces = MediaType.APPLICATION_XML_VALUE)
	public String wxPayRefund(@RequestBody String xmlString) {
		return wxNotifyFeign.wxPayRefundNotify(xmlString).dataOrThrow();
	}

	@PostMapping(value = "/aliPay")
	public String aliPay(HttpServletRequest request) {
		Map<String, String> params = getReqParam(request);
		return aliNotifyFeign.aliPayNotify(params).dataOrThrow();
	}

	/**
	 * spi.alipay.marketing.activity.order.send(订单券发放)
	 */
	@PostMapping("/spiOrderSend")
	public SpiOrderSendNotifyResp spiOrderSend(HttpServletRequest request) {
		Map<String, String> params = getReqParam(request);
		return aliActivityNotifyFeign.spiOrderSendNotify(params).dataOrThrow();
	}

	/**
	 * From平台消息
	 */
	@PostMapping("/from")
	public String from(HttpServletRequest request) {
		Map<String, String> params = getReqParam(request);
		return aliActivityNotifyFeign.fromNotify(params).dataOrThrow();
	}

	/**
	 * 组装request中的参数
	 * 
	 * <pre>
	 * 以下场景都能通过request.getParameterNames获取参数
	 * 1.参数跟在url后面
	 * 2.POST form-data
	 * 3.POST x-www-form-urlencoded
	 * </pre>
	 * 
	 * @param request
	 * @return
	 */
	private Map<String, String> getReqParam(HttpServletRequest request) {
		Enumeration<String> parameterNames = request.getParameterNames();
		Map<String, String> paramMap = new HashMap<>();
		while (parameterNames.hasMoreElements()) {
			String name = parameterNames.nextElement();
			paramMap.put(name, request.getParameter(name));
		}
		return paramMap;
	}
}
