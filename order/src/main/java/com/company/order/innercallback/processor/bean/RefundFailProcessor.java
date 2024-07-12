package com.company.order.innercallback.processor.bean;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.company.common.util.JsonUtil;
import com.company.order.api.enums.OrderPayRefundEnum;
import com.company.order.api.request.RefundNotifyReq;
import com.company.order.api.request.RefundReq;
import com.company.order.entity.OrderPayRefund;
import com.company.order.innercallback.processor.AbandonRequestProcessor;
import com.company.order.innercallback.service.IInnerCallbackService;
import com.company.order.innercallback.service.PostParam;
import com.company.order.service.OrderPayRefundService;

import lombok.extern.slf4j.Slf4j;

/**
 * 退款失败后的处理类
 */
@Slf4j
@Component(InnerCallbackProcessorBeanName.REFUND_FAIL_PROCESSOR)
public class RefundFailProcessor implements AbandonRequestProcessor {

    @Autowired
    private OrderPayRefundService orderPayRefundService;

    @Autowired
    private IInnerCallbackService innerCallbackService;

    @Override
    public void afterAbandonRequest(String jsonParams, String abandonReason) {
        log.info("refundFailProcessor afterAbandonRequest, params is {}", jsonParams);

        RefundReq refundReq = JsonUtil.toEntity(jsonParams, RefundReq.class);
        String refundOrderCode = refundReq.getRefundOrderCode();

        // 退款失败
        OrderPayRefund orderPayRefund = orderPayRefundService.selectByRefundOrderCode(refundOrderCode);

        OrderPayRefund orderPayRefund4Update = new OrderPayRefund();
        orderPayRefund4Update.setId(orderPayRefund.getId());
        orderPayRefund4Update.setStatus(OrderPayRefundEnum.Status.REFUND_FAIL.getCode());
        orderPayRefund4Update.setRemark(abandonReason);
        orderPayRefundService.updateById(orderPayRefund4Update);

        // 回调退款到对应业务中
        String notifyUrl = orderPayRefund.getNotifyUrl();
        if (StringUtils.isBlank(notifyUrl)) {
            log.info("refundFailProcessor afterAbandonRequest, notifyUrl is blank");
            return;
        }

        RefundNotifyReq refundNotifyReq = new RefundNotifyReq();
        refundNotifyReq.setSuccess(Boolean.FALSE);
        refundNotifyReq.setMessage(abandonReason);
        refundNotifyReq.setOrderCode(orderPayRefund.getOrderCode());
        refundNotifyReq.setRefundOrderCode(refundOrderCode);
        refundNotifyReq.setAttach(orderPayRefund.getNotifyAttach());

		PostParam postParam = PostParam.builder().notifyUrl(notifyUrl).jsonParams(refundNotifyReq).build();
		innerCallbackService.postRestTemplate(postParam);
    }
}
