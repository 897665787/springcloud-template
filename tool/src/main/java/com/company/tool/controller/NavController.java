package com.company.tool.controller;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.company.common.api.Result;
import com.company.common.util.JsonUtil;
import com.company.framework.context.HttpContextUtil;
import com.company.tool.api.feign.NavFeign;
import com.company.tool.api.request.NavReq;
import com.company.tool.api.response.NavResp;
import com.company.tool.nav.NavShowService;
import com.company.tool.nav.dto.NavItemCanShow;
import com.company.user.api.enums.UserOauthEnum;
import com.company.user.api.feign.UserOauthFeign;
import com.company.user.api.response.UserOauthResp;

@RestController
@RequestMapping("/nav")
public class NavController implements NavFeign {

	@Autowired
	private NavShowService navShowService;
	@Autowired
	private UserOauthFeign userOauthFeign;

	@Override
	public Result<List<NavResp>> list(@RequestBody NavReq navReq) {
		Map<String, String> runtimeAttach = navReq.getRuntimeAttach();

		// 补充一些请求头参数
		runtimeAttach.putAll(HttpContextUtil.httpContextHeader());
		
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
		runtimeAttach.put("{userId}", userId);

		List<NavItemCanShow> navList = navShowService.list(navReq.getMaxSize(), runtimeAttach);
		List<NavResp> respList = navList.stream().map(v -> {
			NavResp resp = new NavResp();
			resp.setTitle(v.getTitle());
			resp.setLogo(v.getLogo());
			resp.setType(v.getType());
			resp.setValue(v.getValue());
			String attachJson = v.getAttachJson();
			if (StringUtils.isNotBlank(attachJson)) {
				resp.setAttach(JsonUtil.toJsonNode(attachJson));
			}
			return resp;
		}).collect(Collectors.toList());

		return Result.success(respList);
	}
}
