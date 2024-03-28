package com.company.app.controller;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.company.common.api.Result;
import com.company.framework.context.HttpContextUtil;
import com.company.tool.api.feign.BannerFeign;
import com.company.tool.api.feign.NavFeign;
import com.company.tool.api.request.BannerReq;
import com.company.tool.api.request.NavReq;
import com.company.tool.api.response.BannerResp;
import com.company.tool.api.response.NavResp;
import com.company.user.api.enums.UserOauthEnum;
import com.company.user.api.feign.UserOauthFeign;
import com.company.user.api.response.UserOauthResp;

/**
 * 导航栏API
 * 
 * @author JQ棣
 */
@RestController
@RequestMapping("/navigation")
public class NavigationController {

	@Autowired
	private NavFeign navFeign;
	@Autowired
	private BannerFeign bannerFeign;
	@Autowired
	private UserOauthFeign userOauthFeign;

	/**
	 * 金刚位列表
	 */
	@RequestMapping("/nav")
	public Result<List<NavResp>> nav(HttpServletRequest request) {
		
		NavReq navReq = new NavReq();
		
		Map<String, String> runtimeAttach = this.getReqParam(request);

		/* 补充一些系统可自动获取的参数 */
		// 需要取token的话可能要在最外层取
		// if (!runtimeAttach.containsKey("token")) {
		// String token = HttpContextUtil.head("x-token");
		// runtimeAttach.put("token", token);
		// }
		
		/* 补充一些展示替换的参数 */
		
		// 补充用户id
		String userId = HttpContextUtil.currentUserId();
		if (StringUtils.isBlank(userId)) {
			String deviceid = HttpContextUtil.deviceid();
			if (StringUtils.isNotBlank(deviceid)) {
				UserOauthResp userOauthResp = userOauthFeign
						.selectOauth(UserOauthEnum.IdentityType.WX_OPENID_MINIAPP, deviceid).dataOrThrow();
				if (userOauthResp != null) {
					userId = String.valueOf(userOauthResp.getUserId());
				}
			}
		}
		runtimeAttach.put("userId", userId);
		
		navReq.setRuntimeAttach(runtimeAttach);
		navReq.setMaxSize(4);

		return navFeign.list(navReq);
	}
	
	/**
	 * 轮播图列表
	 */
	@RequestMapping("/banner")
	public Result<List<BannerResp>> banner(HttpServletRequest request) {
		
		BannerReq bannerReq = new BannerReq();
		
		Map<String, String> runtimeAttach = this.getReqParam(request);

		/* 补充一些系统可自动获取的参数 */
		// 需要取token的话可能要在最外层取
		// if (!runtimeAttach.containsKey("token")) {
		// String token = HttpContextUtil.head("x-token");
		// runtimeAttach.put("token", token);
		// }
		
		/* 补充一些展示替换的参数 */
		
		// 补充用户id
		String userId = HttpContextUtil.currentUserId();
		if (StringUtils.isBlank(userId)) {
			String deviceid = HttpContextUtil.deviceid();
			if (StringUtils.isNotBlank(deviceid)) {
				UserOauthResp userOauthResp = userOauthFeign
						.selectOauth(UserOauthEnum.IdentityType.WX_OPENID_MINIAPP, deviceid).dataOrThrow();
				if (userOauthResp != null) {
					userId = String.valueOf(userOauthResp.getUserId());
				}
			}
		}
		runtimeAttach.put("userId", userId);
		
		bannerReq.setRuntimeAttach(runtimeAttach);
		
		return bannerFeign.list(bannerReq);
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
