package com.company.tool.subscribe.tool.impl;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.company.framework.context.SpringContextUtil;
import com.company.tool.subscribe.dto.SubscribeTemplateInfo;
import com.company.tool.subscribe.tool.IMaTool;
import com.company.tool.subscribe.tool.dto.MaSubscribe;
import com.company.tool.subscribe.tool.dto.SubscribeMsgData;
import com.company.tool.wx.miniapp.config.WxMaConfiguration;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.api.WxMaSubscribeService;
import cn.binarywang.wx.miniapp.bean.WxMaSubscribeMessage;
import cn.binarywang.wx.miniapp.constant.WxMaConstants;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.bean.subscribemsg.TemplateInfo;
import me.chanjar.weixin.common.error.WxError;
import me.chanjar.weixin.common.error.WxErrorException;

@Slf4j
@Component
public class MaTool implements IMaTool {

	@Override
	public MaSubscribe sendSubscribeMsg(String appid, String openid, String templateId, String page,
			List<SubscribeMsgData> dataList) {
		MaSubscribe maSubscribe = new MaSubscribe();

		WxMaService wxMaService = WxMaConfiguration.getMaService(appid);
		WxMaSubscribeService subscribeService = wxMaService.getSubscribeService();

		/**
		 * <pre>
		{
			"touser": "OPENID",
			"template_id": "TEMPLATE_ID",
			"page": "index",
			"miniprogram_state": "developer",
			"lang": "zh_CN",
			"data": {
				"number01": {
					"value": "339208499"
				},
				"date01": {
					"value": "2015年01月05日"
				},
				"site01": {
					"value": "TIT创意园"
				},
				"site02": {
					"value": "广州市新港中路397号"
				}
			}
		}
		 * </pre>
		 */
		WxMaSubscribeMessage subscribeMessage = new WxMaSubscribeMessage();
		subscribeMessage.setToUser(openid);
		subscribeMessage.setTemplateId(templateId);
		subscribeMessage.setPage(page);

		subscribeMessage.setMiniprogramState(WxMaConstants.MiniProgramState.FORMAL);// 默认
		if (SpringContextUtil.isTestProfile()) {// 测试环境
			subscribeMessage.setMiniprogramState(WxMaConstants.MiniProgramState.DEVELOPER);
		}

		List<WxMaSubscribeMessage.MsgData> data = dataList.stream().map(v -> {
			WxMaSubscribeMessage.MsgData msgData = new WxMaSubscribeMessage.MsgData();
			msgData.setName(v.getName());
			msgData.setValue(v.getValue());
			return msgData;
		}).collect(Collectors.toList());
		subscribeMessage.setData(data);

		try {
			/**
			 * 发送订阅消息
			 * 
			 * <pre>
			 * 官网：https://developers.weixin.qq.com/miniprogram/dev/OpenApiDoc/mp-message-management/subscribe-message/sendMessage.html
			 * </pre>
			 */
			subscribeService.sendSubscribeMsg(subscribeMessage);
			maSubscribe.setSuccess(true);
		} catch (WxErrorException e) {
			log.error("sendSubscribeMsg error", e);
			WxError error = e.getError();
			maSubscribe.setSuccess(false);
			maSubscribe.setMessage(error.getErrorMsg());
		}
		return maSubscribe;
	}

	@Override
	public List<SubscribeTemplateInfo> getTemplateList(String appid) {
		WxMaService wxMaService = WxMaConfiguration.getMaService(appid);
		WxMaSubscribeService subscribeService = wxMaService.getSubscribeService();

		try {
			/**
			 * 获取个人模板列表
			 * 
			 * <pre>
			 * 官网：https://developers.weixin.qq.com/miniprogram/dev/OpenApiDoc/mp-message-management/subscribe-message/getMessageTemplateList.html
			 * </pre>
			 */
			List<TemplateInfo> templateList = subscribeService.getTemplateList();
			List<SubscribeTemplateInfo> subscribeTemplateInfoList = templateList.stream().map(v -> {
				SubscribeTemplateInfo subscribeTemplateInfo = new SubscribeTemplateInfo();
				subscribeTemplateInfo.setPriTmplId(v.getPriTmplId());
				subscribeTemplateInfo.setTitle(v.getTitle());
				subscribeTemplateInfo.setContent(v.getContent());
				subscribeTemplateInfo.setExample(v.getExample());
				subscribeTemplateInfo.setType(v.getType());
				return subscribeTemplateInfo;
			}).collect(Collectors.toList());

			return subscribeTemplateInfoList;
		} catch (WxErrorException e) {
			log.error("getTemplateList error", e);
		}
		return Collections.emptyList();
	}
}
