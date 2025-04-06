package com.company.im.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.company.common.api.Result;
import com.company.framework.util.IpUtil;
import com.github.linyuzai.connection.loadbalance.core.extension.GroupMessage;
import com.github.linyuzai.connection.loadbalance.core.extension.PathMessage;
import com.github.linyuzai.connection.loadbalance.core.extension.UserMessage;
import com.github.linyuzai.connection.loadbalance.websocket.concept.WebSocketLoadBalanceConcept;

@RestController
@RequestMapping("/concept")
public class ConceptController {

	@Value("${server.port:0}")
	private String port;

	@Autowired(required = false)
	private WebSocketLoadBalanceConcept concept;

	@RequestMapping("/toUser")
	public Result<?> conceptsend(String toUserId, String msg) {
		String message = IpUtil.getHostIp() + ":" + port + " " + msg;
		if (StringUtils.isBlank(toUserId)) {
			concept.send(message);
		} else {
			concept.send(new UserMessage(message, toUserId));
		}
		return Result.success(message);
	}

	@RequestMapping("/path")
	public Result<?> conceptpath(String path, String msg) {
		String message = IpUtil.getHostIp() + ":" + port + " " + msg;
		if (StringUtils.isBlank(path)) {
			concept.send(message);
		} else {
			concept.send(new PathMessage(message, path));
		}
		return Result.success(message);
	}

	@RequestMapping("/group")
	public Result<?> conceptgroup(String group, String msg) {
		String message = IpUtil.getHostIp() + ":" + port + " " + msg;
		if (StringUtils.isBlank(group)) {
			concept.send(message);
		} else {
			concept.send(new GroupMessage(message, group));// 框架未实现
		}
		return Result.success(message);
	}
}
