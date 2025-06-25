package com.company.web.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.company.framework.messagedriven.MessageSender;
import com.company.framework.messagedriven.constants.FanoutConstants;
import com.company.web.messagedriven.Constants;
import com.company.web.messagedriven.strategy.StrategyConstants;
import com.google.common.collect.Maps;

import cn.hutool.core.date.DateUtil;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/mq")
@Slf4j
public class MqController {
	@Autowired
	private MessageSender messageSender;

	@GetMapping(value = "/sendNormalMessage")
	public String sendNormalMessage(String message) {
		Map<String, Object> params = Maps.newHashMap();
		params.put("open", message);
		params.put("open2", message);
		params.put("time", DateUtil.now());
		messageSender.sendNormalMessage(StrategyConstants.MAP_STRATEGY,params, Constants.EXCHANGE.DIRECT, Constants.QUEUE.COMMON.KEY);

//		UserMQDto param = new UserMQDto();
//		param.setP1("p1");
//		param.setP2("p2");
//		param.setP3("p3");
//		param.put("time", DateUtil.now());
//		messageSender.sendNormalMessage(StrategyConstants.USER_STRATEGY, param, Constants.EXCHANGE.DIRECT, Constants.QUEUE.COMMON.ROUTING_KEY);

		return "success";
	}

	@GetMapping(value = "/sendFanoutMessage")
	public String sendFanoutMessage(String message) {
		Map<String, Object> params = Maps.newHashMap();
		params.put("open", message);
		params.put("open2", message);
		params.put("time", DateUtil.now());
		messageSender.sendFanoutMessage(params, FanoutConstants.ORDER_CREATE.EXCHANGE);

		return "success";
	}

	@GetMapping(value = "/sendDelayMessage")
	public String sendDelayMessage(String message, Integer delaySeconds) {
		Map<String, Object> params = Maps.newHashMap();
		params.put("open", message);
		params.put("time", DateUtil.now());
		params.put("delaySeconds", delaySeconds);

		messageSender.sendDelayMessage(StrategyConstants.XDELAYMESSAGE_STRATEGY, params, Constants.EXCHANGE.DIRECT,
				Constants.QUEUE.DEAD_LETTER.KEY, delaySeconds);
		return "success";
	}

	@GetMapping(value = "/sendXDelayMessage")
	public String sendXDelayMessage(String message, Integer delaySeconds) {
		Map<String, Object> params = Maps.newHashMap();
		params.put("open", message);
		params.put("time", DateUtil.now());
		params.put("delaySeconds", delaySeconds);

		messageSender.sendDelayMessage(StrategyConstants.XDELAYMESSAGE_STRATEGY, params, Constants.EXCHANGE.XDELAYED,
				Constants.QUEUE.XDELAYED.KEY, delaySeconds);
		return "success";
	}
}
