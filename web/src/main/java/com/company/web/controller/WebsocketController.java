package com.company.web.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.tio.core.Tio;
import org.tio.websocket.common.WsResponse;
import org.tio.websocket.starter.TioWebSocketServerBootstrap;

import com.company.common.api.Result;
import com.company.framework.util.IpUtil;
import com.github.linyuzai.connection.loadbalance.core.extension.GroupMessage;
import com.github.linyuzai.connection.loadbalance.core.extension.PathMessage;
import com.github.linyuzai.connection.loadbalance.core.extension.UserMessage;
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

	@RequestMapping("/concept/toUser")
	@ResponseBody
	public Result<?> conceptsend(String toUserId, String msg) {
		String message = IpUtil.getHostIp() + ":" + port + " " + msg;
		if (StringUtils.isBlank(toUserId)) {
			concept.send(message);
		} else {
			concept.send(new UserMessage(message, toUserId));
		}
		return Result.success(message);
	}

	@RequestMapping("/concept/path")
	@ResponseBody
	public Result<?> conceptpath(String path, String msg) {
		String message = IpUtil.getHostIp() + ":" + port + " " + msg;
		if (StringUtils.isBlank(path)) {
			concept.send(message);
		} else {
			concept.send(new PathMessage(message, path));
		}
		return Result.success(message);
	}
	
	@RequestMapping("/concept/group")
	@ResponseBody
	public Result<?> conceptgroup(String group, String msg) {
		String message = IpUtil.getHostIp() + ":" + port + " " + msg;
		if (StringUtils.isBlank(group)) {
			concept.send(message);
		} else {
			concept.send(new GroupMessage(message, group));// 框架未实现
		}
		return Result.success(message);
	}

	@GetMapping("/tio/{userId}")
	public String tio(@PathVariable String userId, ModelAndView view) {
		view.addObject("userId", userId);
		return "websocket/tio";
	}

	@Autowired
	private TioWebSocketServerBootstrap bootstrap;

	@RequestMapping("/tio/toUser")
	@ResponseBody
	public Result<?> tiosend(String toUserId, String msg) {
		String message = IpUtil.getHostIp() + ":" + port + " " + msg;
		if (StringUtils.isBlank(toUserId)) {
			Tio.sendToAll(bootstrap.getServerTioConfig(), WsResponse.fromText(message, "utf-8"));
		} else {
			Tio.sendToUser(bootstrap.getServerTioConfig(), toUserId, WsResponse.fromText(message, "utf-8"));
		}
		return Result.success(message);
	}

	@RequestMapping("/tio/group")
	@ResponseBody
	public Result<?> tiogroup(String group, String msg) {
		String message = IpUtil.getHostIp() + ":" + port + " " + msg;
		if (StringUtils.isBlank(group)) {
			Tio.sendToAll(bootstrap.getServerTioConfig(), WsResponse.fromText(message, "utf-8"));
		} else {
			Tio.sendToGroup(bootstrap.getServerTioConfig(), group, WsResponse.fromText(message, "utf-8"));
		}
		return Result.success(message);
	}
}
