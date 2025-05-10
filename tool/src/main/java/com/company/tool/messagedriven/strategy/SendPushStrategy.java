//package com.company.tool.messagedriven.strategy;
//
//import com.company.framework.messagedriven.BaseStrategy;
//import com.company.tool.messagedriven.strategy.dto.SendPushMQDto;
//import com.company.tool.push.PushSenderConsumer;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//@Component(StrategyConstants.SENDSMS_STRATEGY)
//public class SendPushStrategy implements BaseStrategy<SendPushMQDto> {
//
//	@Autowired
//	private PushSenderConsumer pushSenderConsumer;
//
//	@Override
//	public void doStrategy(SendPushMQDto params) {
//		Integer pushTaskDetailId = params.getPushTaskDetailId();
//		pushSenderConsumer.consumer(pushTaskDetailId);
//	}
//}
