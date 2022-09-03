//package com.company.order.rabbitmq.consumer.strategy;
//
//import java.util.Map;
//
//import com.company.order.api.request.RefundReq;
//import com.yeahka.lsq.orderservice.service.innercallback.processor.bean.InnerCallbackProcessorBeanName;
//import com.yeahka.lsq.orderservice.service.innercallback.processor.bean.ProcessorBeanName;
//import org.apache.commons.collections4.MapUtils;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//import com.yeahka.common.online.rabbitmq.strategy.BaseStrategy;
//import com.yeahka.lsq.orderservice.service.IInnerCallbackService;
//
//@Component(StrategyConstants.REFUND_STRATEGY)
//public class RefundStrategy implements BaseStrategy<Map<String, Object>> {
//
//	@Autowired
//	private IInnerCallbackService innerCallbackService;
//
//	private static final String NOTIFY_URL_REFUND = "http://template-order/pay/refundWithRetry";
//
//	@Override
//	public void doStrategy(Map<String, Object> params) {
//		String outRefundNo = MapUtils.getString(params, "outRefundNo");
//		RefundReq refundReq = new RefundReq();
//		refundReq.setOutRefundNo(outRefundNo);
//
//		ProcessorBeanName processorBeanName = new ProcessorBeanName();
//		processorBeanName.setAbandonRequest(InnerCallbackProcessorBeanName.REFUND_FAIL_PROCESSOR);
//
//		innerCallbackService.postRestTemplate(NOTIFY_URL_REFUND, refundReq, processorBeanName);
//	}
//}
