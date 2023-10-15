package com.company.tool.subscribe;

import java.util.Map;

import com.company.tool.subscribe.dto.SubscribeSendDto;

public interface SubscribeType {

	/**
	 * 包装发送订阅消息的参数
	 * 
	 * @param userId
	 * @param runtimeAttach
	 * @return
	 */
	SubscribeSendDto wrapParam(Integer userId, Map<String, String> runtimeAttach);

}
