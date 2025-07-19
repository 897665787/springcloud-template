package com.company.tool.subscribe.tool;

import java.util.List;

import com.company.tool.subscribe.dto.SubscribeTemplateInfo;
import com.company.tool.subscribe.tool.dto.MaSubscribe;
import com.company.tool.subscribe.tool.dto.SubscribeMsgData;

public interface IMaTool {

	MaSubscribe sendSubscribeMsg(String appid, String openid, String templateId, String page,
			List<SubscribeMsgData> dataList);

	List<SubscribeTemplateInfo> getTemplateList(String appid);
}
