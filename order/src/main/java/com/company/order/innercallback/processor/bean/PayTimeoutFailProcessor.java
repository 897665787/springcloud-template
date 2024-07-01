package com.company.order.innercallback.processor.bean;

import java.time.LocalDateTime;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.company.common.util.JsonUtil;
import com.company.order.api.enums.OrderPayEnum;
import com.company.order.api.request.PayNotifyReq;
import com.company.order.api.request.PayTimeoutReq;
import com.company.order.entity.OrderPay;
import com.company.order.innercallback.processor.AbandonRequestProcessor;
import com.company.order.innercallback.service.IInnerCallbackService;
import com.company.order.innercallback.service.PostParam;
import com.company.order.service.OrderPayService;

import lombok.extern.slf4j.Slf4j;

/**
 * 支付超时处理失败后的处理类
 */
@Slf4j
@Component(InnerCallbackProcessorBeanName.PAYTIMEOUT_FAIL_PROCESSOR)
public class PayTimeoutFailProcessor implements AbandonRequestProcessor {

    @Autowired
    private IInnerCallbackService innerCallbackService;
    @Autowired
    private OrderPayService orderPayService;

    @Override
    public void afterAbandonRequest(String jsonParams, String abandonReason) {
        log.info("payTimeoutFailProcessor afterAbandonRequest, params is {}", jsonParams);

        PayTimeoutReq payTimeoutReq = JsonUtil.toEntity(jsonParams, PayTimeoutReq.class);
        String orderCode = payTimeoutReq.getOrderCode();

		OrderPay orderPay = orderPayService.selectByOrderCode(orderCode);
		if (orderPay == null) {
			return;
		}
        
		// 修改状态为已关闭
		OrderPay orderPay4Update = new OrderPay();
		orderPay4Update.setStatus(OrderPayEnum.Status.CLOSED.getCode());
		LocalDateTime time = LocalDateTime.now();
		orderPay4Update.setPayTime(time);
		EntityWrapper<OrderPay> wrapper = new EntityWrapper<>();
		wrapper.eq("id", orderPay.getId());
		wrapper.eq("status", OrderPayEnum.Status.WAITPAY.getCode());
		boolean affect = orderPayService.update(orderPay4Update, wrapper);
		if (!affect) {// 更新不成功，说明订单不是处理中状态
			log.info("update订单不是处理中状态，不操作关闭:{}", orderCode);
			return;
		}

		String notifyUrl = orderPay.getNotifyUrl();
		if (StringUtils.isBlank(notifyUrl)) {
			log.info("无回调URL");
			return;
		}

		// 回调超时未支付关闭订单到对应业务中
		PayNotifyReq payNotifyReq = new PayNotifyReq();
		payNotifyReq.setEvent(PayNotifyReq.EVENT.CLOSE);
		payNotifyReq.setOrderCode(orderCode);
		payNotifyReq.setTime(time);
		payNotifyReq.setAttach(orderPay.getNotifyAttach());

		log.info("超时未支付关闭订单回调,请求地址:{},参数:{}", notifyUrl, JsonUtil.toJsonString(payNotifyReq));
		PostParam postParam = PostParam.builder().notifyUrl(notifyUrl).jsonParams(payNotifyReq).build();
		innerCallbackService.postRestTemplate(postParam);

    }
}
