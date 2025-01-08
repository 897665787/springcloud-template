package com.company.web.controller;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.spring.annotation.ConsumeMode;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.apache.rocketmq.spring.core.RocketMQPushConsumerLifecycleListener;
import org.springframework.stereotype.Service;

@Service
@RocketMQMessageListener(
		topic = "topic-test",
		consumerGroup = "topic-test" + "-common",
		consumeMode = ConsumeMode.ORDERLY,
		consumeThreadMax = 20
)
@Slf4j
public class ApiControllerList implements RocketMQListener<String>, RocketMQPushConsumerLifecycleListener {

	@Override
	public void onMessage(String s) {
		log.info("onMessage: {}", s);
		System.out.printf("onMessage: %s %n", s);
	}

	@Override
	public void prepareStart(DefaultMQPushConsumer defaultMQPushConsumer) {
		defaultMQPushConsumer.setMaxReconsumeTimes(2);
	}
}