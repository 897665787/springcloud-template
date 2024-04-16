package com.company.web.websocket.tio;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.tio.core.ChannelContext;
import org.tio.core.Tio;
import org.tio.core.TioConfig;
import org.tio.core.intf.Packet;
import org.tio.http.common.HttpRequest;
import org.tio.http.common.HttpResponse;
import org.tio.websocket.common.WsRequest;
import org.tio.websocket.common.WsResponse;
import org.tio.websocket.server.handler.IWsMsgHandler;

import com.company.common.util.JsonUtil;
import com.company.web.websocket.dto.WsMsg;

import lombok.extern.slf4j.Slf4j;

/**
 * tio网络套接字味精处理程序
 *
 * @author xssq
 * @version 1.0.0
 * @date 2023/09/26
 * @wisdom 你可以不会，但你不能不知道
 */
@Slf4j
@Component
public class TioWebSocketMsgHandler implements IWsMsgHandler {

	/**
	 * 握手
	 *
	 * @param httpRequest
	 *            http请求
	 * @param httpResponse
	 *            http响应
	 * @param channelContext
	 *            渠道上下文
	 * @return {@link HttpResponse}
	 */
	@Override
	public HttpResponse handshake(HttpRequest httpRequest, HttpResponse httpResponse, ChannelContext channelContext) {
		String id = channelContext.getId();
		String bsId = channelContext.getBsId();
		String token = channelContext.getToken();
		log.info("handshake,id:{},bsId:{},token:{}", id, bsId, token);

		String token2 = httpRequest.getParam("token");
		channelContext.setToken(token2);

		Tio.bindBsId(channelContext, httpRequest.getParam("bsId"));
		Tio.bindUser(channelContext, httpRequest.getParam("userId"));

		// Tio.bindGroup(channelContext, "1");

		System.out.println("handshake握手方法");
		return httpResponse;
	}

	/**
	 * 握手后打开
	 *
	 * @param httpRequest
	 *            http请求
	 * @param httpResponse
	 *            http响应
	 * @param channelContext
	 *            渠道上下文
	 */
	@Override
	public void onAfterHandshaked(HttpRequest httpRequest, HttpResponse httpResponse, ChannelContext channelContext) {
		String id = channelContext.getId();
		String bsId = channelContext.getBsId();
		String token = channelContext.getToken();
		log.info("onAfterHandshaked,id:{},bsId:{},token:{}", id, bsId, token);
	}

	/**
	 * 在字节上
	 *
	 * @param wsRequest
	 *            ws请求
	 * @param bytes
	 *            字节
	 * @param channelContext
	 *            渠道上下文
	 * @return {@link Object}
	 */
	@Override
	public Object onBytes(WsRequest wsRequest, byte[] bytes, ChannelContext channelContext) {
		System.out.println("onBytes方法");
		return null;
	}

	/**
	 * 关闭
	 *
	 * @param wsRequest
	 *            ws请求
	 * @param bytes
	 *            字节
	 * @param channelContext
	 *            渠道上下文
	 * @return {@link Object}
	 */
	@Override
	public Object onClose(WsRequest wsRequest, byte[] bytes, ChannelContext channelContext) {
		String id = channelContext.getId();
		String bsId = channelContext.getBsId();
		String token = channelContext.getToken();
		log.info("onClose,id:{},bsId:{},token:{}", id, bsId, token);
		return null;
	}

	/**
	 * 在文本上
	 *
	 * @param wsRequest
	 *            ws请求
	 * @param channelContext
	 *            渠道上下文
	 * @param message
	 *            信息
	 * @return {@link Object}
	 */
	@Override
	public Object onText(WsRequest wsRequest, String message, ChannelContext channelContext) {
		String id = channelContext.getId();
		String bsId = channelContext.getBsId();
		String token = channelContext.getToken();
		log.info("onText,id:{},bsId:{},token:{},message:{}", id, bsId, token, message);
		if ("心跳包".equals(message)) {
			return null;
		}
		String userid = channelContext.userid;
		TioConfig tioConfig = channelContext.getTioConfig();

		WsMsg wsMsg = JsonUtil.toEntity(message, WsMsg.class);
		String toUserId = wsMsg.getToUserId();
		if (StringUtils.isBlank(toUserId)) {
			String msg = String.format("接收到【%s】的信息:%s", userid, wsMsg.getMessage());
			Packet packet = WsResponse.fromText(msg, "utf-8");
			Tio.sendToAll(tioConfig, packet);
			return String.format("群发成功");
		} else {
			String msg = String.format("接收到【%s】的信息:%s", userid, wsMsg.getMessage());
			Packet packet = WsResponse.fromText(msg, "utf-8");
			Tio.sendToUser(tioConfig, toUserId, packet);
			return String.format("发送给【%s】成功", toUserId);
		}
	}
}