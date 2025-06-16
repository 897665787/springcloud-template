package com.company.im.websocket.tio;

import com.company.framework.trace.TraceManager;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
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
import com.company.im.websocket.dto.WsMsg;

import lombok.extern.slf4j.Slf4j;

/**
 * tio处理程序
 */
@Slf4j
@Component
public class TioWebSocketMsgHandler implements IWsMsgHandler {

	@Autowired
	private TraceManager traceManager;

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
		Tio.bindGroup(channelContext, httpRequest.getParam("group"));

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
		String id = channelContext.getId();
		String bsId = channelContext.getBsId();
		String token = channelContext.getToken();
		log.info("onBytes,id:{},bsId:{},token:{}", id, bsId, token);
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
		traceManager.put();
		String id = channelContext.getId();
		String bsId = channelContext.getBsId();
		String token = channelContext.getToken();
		log.info("onText,id:{},bsId:{},token:{},message:{}", id, bsId, token, message);

		String userid = channelContext.userid;
		TioConfig tioConfig = channelContext.getTioConfig();

		WsMsg wsMsg = JsonUtil.toEntity(message, WsMsg.class);
		String toUserId = wsMsg.getToUserId();
		Object result = null;
		if (StringUtils.isBlank(toUserId)) {
			String msg = String.format("接收到【%s】的信息:%s", userid, wsMsg.getMessage());
			Packet packet = WsResponse.fromText(msg, "utf-8");
			Tio.sendToAll(tioConfig, packet);
			result = String.format("群发成功");
		} else {
			String msg = String.format("接收到【%s】的信息:%s", userid, wsMsg.getMessage());
			Packet packet = WsResponse.fromText(msg, "utf-8");
			Tio.sendToUser(tioConfig, toUserId, packet);
			result = String.format("发送给【%s】成功", toUserId);
		}
		traceManager.remove();
		return result;
	}
}
