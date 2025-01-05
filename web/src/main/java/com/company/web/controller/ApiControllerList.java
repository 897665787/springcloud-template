package com.company.web.controller;

import com.company.common.api.Result;
import com.company.common.util.JsonUtil;
import com.company.framework.amqp.MessageSender;
import com.company.framework.amqp.rabbit.constants.FanoutConstants;
import com.company.framework.annotation.RequireLogin;
import com.company.framework.autoconfigure.ThreadPoolProperties;
import com.company.framework.cache.ICache;
import com.company.framework.context.HttpContextUtil;
import com.company.framework.context.SpringContextUtil;
import com.company.framework.sequence.SequenceGenerator;
import com.company.order.api.feign.OrderFeign;
import com.company.order.api.response.OrderResp;
import com.company.user.api.feign.UserFeign;
import com.company.user.api.response.UserResp;
import com.company.web.amqp.rabbitmq.Constants;
import com.company.web.amqp.strategy.StrategyConstants;
import com.company.web.amqp.strategy.dto.UserMQDto;
import com.company.web.service.TimeService;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.spring.annotation.ConsumeMode;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.apache.rocketmq.spring.core.RocketMQPushConsumerLifecycleListener;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;

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