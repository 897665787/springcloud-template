package com.company.web.controller;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.company.common.api.Result;
import com.company.framework.context.HttpContextUtil;
import com.company.tool.api.feign.SubscribeFeign;
import com.company.tool.api.request.SubscribeGrantReq;
import com.company.web.req.GrantReq;

@RestController
@RequestMapping("/subscribe")
public class SubscribeController {

	@Autowired
	private SubscribeFeign subscribeFeign;

	/**
	 * 获取订阅组
	 * 
	 * @param group
	 * @return
	 */
	@GetMapping(value = "/group")
	public Result<List<String>> group(String group) {
		return subscribeFeign.selectTemplateCodeByGroup(group);
	}

	/**
	 * 提交授权
	 * 
	 * @param request
	 * @param grantReq
	 * @return
	 */
	@PostMapping(value = "/grant")
	public Result<Void> grant(HttpServletRequest request, GrantReq grantReq) {
		SubscribeGrantReq subscribeGrantReq = new SubscribeGrantReq();
		subscribeGrantReq.setGroup(grantReq.getGroup());

		String openid = HttpContextUtil.deviceid();
		subscribeGrantReq.setOpenid(openid);

		subscribeGrantReq.setResJson(grantReq.getResJson());

		Map<String, String> runtimeAttach = getReqParam(request);
		// 移除GrantReq独立处理的参数
		runtimeAttach.remove("group");
		runtimeAttach.remove("resJson");

		subscribeGrantReq.setRuntimeAttach(runtimeAttach);
		return subscribeFeign.grant(subscribeGrantReq);
	}

	/**
	 * 组装request中的参数
	 * 
	 * <pre>
	 * 以下场景都能通过request.getParameterNames获取参数
	 * 1.参数跟在url后面
	 * 2.POST form-data
	 * 3.POST x-www-form-urlencoded
	 * </pre>
	 * 
	 * @param request
	 * @return
	 */
	private Map<String, String> getReqParam(HttpServletRequest request) {
		Enumeration<String> parameterNames = request.getParameterNames();
		Map<String, String> paramMap = new HashMap<>();
		while (parameterNames.hasMoreElements()) {
			String name = parameterNames.nextElement();
			paramMap.put(name, request.getParameter(name));
		}
		return paramMap;
	}
}
