package com.company.app.controller;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.company.common.api.Result;
import com.company.tool.api.feign.PopupFeign;
import com.company.tool.api.request.BestPopupReq;

/**
 * 弹窗API
 * 
 * @author JQ棣
 */
@RestController
@RequestMapping("/popup")
public class PopupController {

	@Autowired
	private PopupFeign popupFeign;

	/**
	 * 用户最优的弹窗(前端唯一入口)
	 */
	@RequestMapping("/best")
	public Result<?> best(HttpServletRequest request) {
		BestPopupReq bestPopupReq = new BestPopupReq();
		
		Map<String, String> runtimeAttach = this.getReqParam(request);

		/* 补充一些系统可自动获取的参数 */
		// 需要取token的话可能要在最外层取
		// if (!runtimeAttach.containsKey("token")) {
		// String token = HttpContextUtil.head("x-token");
		// runtimeAttach.put("token", token);
		// }
		/* 补充一些弹窗替换的参数 */
		
		bestPopupReq.setRuntimeAttach(runtimeAttach);

		return popupFeign.bestPopup(bestPopupReq);
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
	
	/**
	 * 前端确认展示了弹窗
	 */
	@RequestMapping("/display")
	public Result<?> display(Integer popupLogId) {
		popupFeign.remarkPopupLog(popupLogId, "mini展示弹窗");
		return Result.success();
	}
}
