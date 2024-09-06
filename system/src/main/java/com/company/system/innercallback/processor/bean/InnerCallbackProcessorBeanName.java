package com.company.system.innercallback.processor.bean;

public interface InnerCallbackProcessorBeanName {

    /**
     * 退款失败后的处理类
     */
    String REFUND_FAIL_PROCESSOR = "refundFailProcessor";

    /**
     * 支付超时处理失败后的处理类
     */
    String PAYTIMEOUT_FAIL_PROCESSOR = "payTimeoutFailProcessor";

}
