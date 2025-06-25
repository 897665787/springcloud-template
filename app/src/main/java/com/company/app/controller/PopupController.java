package com.company.app.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.company.framework.util.WebUtil;
import com.company.tool.api.feign.PopupFeign;
import com.company.tool.api.request.BestPopupReq;
import com.company.tool.api.response.BestPopupResp;

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
	public BestPopupResp best(HttpServletRequest request) {
		BestPopupReq bestPopupReq = new BestPopupReq();

		Map<String, String> runtimeAttach = WebUtil.getReqParam(request);

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
	 * 前端确认展示了弹窗
	 */
	@RequestMapping("/display")
	public Void display(Integer popupLogId) {
		popupFeign.remarkPopupLog(popupLogId, "mini展示弹窗");
		return null;
	}
}
