package com.company.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.company.common.api.Result;
import com.company.framework.util.IpUtil;
import com.github.linyuzai.connection.loadbalance.core.extension.PathMessage;
import com.github.linyuzai.connection.loadbalance.websocket.concept.WebSocketLoadBalanceConcept;

@Controller
@RequestMapping("/websocket")
public class WebsocketController {

	@Value("${server.port:0}")
	private String port;

	@GetMapping("/concept/{userId}")
	public String concept(@PathVariable String userId, ModelAndView view) {
		view.addObject("userId", userId);
		return "websocket/concept";
	}

	@Autowired(required = false)
	private WebSocketLoadBalanceConcept concept;

	@RequestMapping("/concept/send")
	@ResponseBody
	public Result<?> conceptsend(String toUserId, String msg) {
		String message = IpUtil.getHostIp() + ":" + port + " " + msg;
//		concept.send(message);
		concept.send(new PathMessage(message, toUserId));
		return Result.success(message);
	}

	@GetMapping("/tio/{userId}")
	public String tio(@PathVariable String userId, ModelAndView view) {
		view.addObject("userId", userId);
		return "websocket/tio";
	}
}
