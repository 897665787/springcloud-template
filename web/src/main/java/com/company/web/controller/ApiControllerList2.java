package com.company.web.controller;

import com.company.common.util.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.spring.annotation.ConsumeMode;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.apache.rocketmq.spring.core.RocketMQPushConsumerLifecycleListener;
import org.springframework.stereotype.Service;

import java.nio.charset.Charset;
import java.util.Map;

@Service
@Slf4j
@RocketMQMessageListener(
		topic = "topic-test",
		consumerGroup = "topic-test" + "-common2",
		consumeMode = ConsumeMode.ORDERLY,
		consumeThreadMax = 20
)
public class ApiControllerList2 implements RocketMQListener<MessageExt>, RocketMQPushConsumerLifecycleListener {

	@Override
	public void onMessage(MessageExt s) {
		log.info("onMessage2: {}", s);

		Map<String, String> properties = s.getProperties();
		System.out.println("properties: "+ JsonUtil.toJsonString(properties));

		String str = new String(s.getBody(), Charset.forName("UTF-8"));
		System.out.println("s.getBody():"+str);
	}

	@Override
	public void prepareStart(DefaultMQPushConsumer defaultMQPushConsumer) {
		defaultMQPushConsumer.setMaxReconsumeTimes(2);
	}
}