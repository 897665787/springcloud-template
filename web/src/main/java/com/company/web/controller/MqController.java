package com.company.web.controller;

import cn.hutool.core.date.DateUtil;
import com.company.common.api.Result;
import com.company.messagedriven.MessageSender;
import com.company.messagedriven.QueueProperties;
import com.company.messagedriven.constants.FanoutConstants;
import com.company.web.messagedriven.strategy.StrategyConstants;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/mq")
@Slf4j
public class MqController {
	@Autowired
	private MessageSender messageSender;
	@Autowired
	private QueueProperties queueProperties;

	@GetMapping(value = "/sendNormalMessage")
	public Result<String> sendNormalMessage(String message) {
		Map<String, Object> params = Maps.newHashMap();
		params.put("open", message);
		params.put("open2", message);
		params.put("time", DateUtil.now());
		messageSender.sendNormalMessage(StrategyConstants.MAP_STRATEGY, params, queueProperties.getExchange().getDirect(), queueProperties.getQueue().getCommon().getKey());

//		UserMQDto param = new UserMQDto();
//		param.setP1("p1");
//		param.setP2("p2");
//		param.setP3("p3");
//		param.put("time", DateUtil.now());
//		messageSender.sendNormalMessage(StrategyConstants.USER_STRATEGY, param, queueProperties.getExchange().getDirect(), Constants.QUEUE.COMMON.ROUTING_KEY);

		return Result.success("success");
	}

	@GetMapping(value = "/sendFanoutMessage")
	public Result<String> sendFanoutMessage(String message) {
		Map<String, Object> params = Maps.newHashMap();
		params.put("open", message);
		params.put("open2", message);
		params.put("time", DateUtil.now());
		messageSender.sendFanoutMessage(params, FanoutConstants.ORDER_CREATE.EXCHANGE);

		return Result.success("success");
	}

	@GetMapping(value = "/sendDelayMessage")
	public Result<String> sendDelayMessage(String message, Integer delaySeconds) {
		Map<String, Object> params = Maps.newHashMap();
		params.put("open", message);
		params.put("time", DateUtil.now());
		params.put("delaySeconds", delaySeconds);

		messageSender.sendDelayMessage(StrategyConstants.XDELAYMESSAGE_STRATEGY, params, queueProperties.getExchange().getDirect(),
                queueProperties.getQueue().getDead_letter().getKey(), delaySeconds);
		return Result.success("success");
	}
	
	@GetMapping(value = "/sendXDelayMessage")
	public Result<String> sendXDelayMessage(String message, Integer delaySeconds) {
		Map<String, Object> params = Maps.newHashMap();
		params.put("open", message);
		params.put("time", DateUtil.now());
		params.put("delaySeconds", delaySeconds);

		messageSender.sendDelayMessage(StrategyConstants.XDELAYMESSAGE_STRATEGY, params, queueProperties.getExchange().getXdelayed(),
                queueProperties.getQueue().getXdelayed().getKey(), delaySeconds);
		return Result.success("success");
	}
}
