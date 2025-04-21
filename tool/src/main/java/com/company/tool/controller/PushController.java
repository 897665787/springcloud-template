package com.company.tool.controller;

import com.company.common.api.Result;
import com.company.common.util.JsonUtil;
import com.company.framework.context.HttpContextUtil;
import com.company.tool.api.enums.PushEnum;
import com.company.tool.api.enums.SubscribeEnum;
import com.company.tool.api.feign.PushFeign;
import com.company.tool.api.request.BindDeviceReq;
import com.company.tool.api.request.SendPushReq;
import com.company.tool.push.AsyncPushSender;
import com.company.tool.push.core.Constants;
import com.company.tool.push.core.PushSender;
import com.company.tool.push.core.repository.PushInfo;
import com.company.tool.subscribe.SubscribeType;
import com.company.tool.subscribe.SubscribeTypeBeanFactory;
import com.company.tool.subscribe.dto.SubscribeSendDto;
import com.company.user.api.enums.UserOauthEnum;
import com.company.user.api.response.UserOauthResp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping(value = "/push")
public class PushController implements PushFeign {

	@Autowired
	private AsyncPushSender asyncPushSender;
	@Autowired
	private PushSender pushSender;

	@Override
	public Result<List<Integer>> select4PreTimeSend(Integer limit) {
		List<Integer> idList = asyncPushSender.select4PreTimeSend(limit);
		return Result.success(idList);
	}
	
	@Override
	public Result<Void> exePreTimeSend(Integer id) {
		asyncPushSender.exePreTimeSend(id);
		return Result.success();
	}

	@Override
	public Result<Void> bindDevice(@RequestBody BindDeviceReq bindDeviceReq) {
		String deviceid = bindDeviceReq.getDeviceid();
		String pushId = bindDeviceReq.getPushId();

		String operator = HttpContextUtil.operator();// ios(iOS)、mac(iOS)、android(Android)、win(Windows,如果获取不到可以不要)
		Constants.DeviceType deviceType = null;
		if ("ios".equalsIgnoreCase(operator) || "mac".equalsIgnoreCase(operator)) {
			deviceType = Constants.DeviceType.iOS;
		} else if ("android".equalsIgnoreCase(operator)) {
			deviceType = Constants.DeviceType.Android;
		} else {
			throw new IllegalArgumentException("不支持的操作系统类型:" + operator);
		}
		pushSender.bindDevice(deviceid, pushId, deviceType);
		return Result.success();
	}

	@Override
	public Result<Void> send(@RequestBody SendPushReq sendPushReq) {
		String mobile = sendPushReq.getDeviceid();
		Map<String, String> templateParamMap = sendPushReq.getTemplateParamMap();
		LocalDateTime planSendTime = sendPushReq.getPlanSendTime();
		PushEnum.Type type = sendPushReq.getType();
		if (planSendTime == null) {
			planSendTime = LocalDateTime.now();
		}
		LocalDateTime overTime = sendPushReq.getOverTime();
		if (overTime == null) {
			overTime = planSendTime.plusDays(1);
		}

		asyncPushSender.send(mobile, templateParamMap, type, planSendTime, overTime);
		return Result.success();
	}
}
