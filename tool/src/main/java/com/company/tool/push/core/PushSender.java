package com.company.tool.push.core;

/**
 * 推送发送器
 */
public interface PushSender {
	/**
	 * 绑定设备
	 */
	void bindDevice(String deviceid, String pushId, Constants.DeviceType deviceType);

	/**
	 * 获取调用渠道
	 *
	 * @return 调用渠道 Constants.Channel
	 */
//	String channel();

	/**
	 * 发送推送
	 *
	 * @param deviceid
	 *            设备ID
	 * @param title
	 *            标题
	 * @param content
	 *            推送内容
	 * @param intent
	 *            跳转意向
	 * @return
	 */
	SendResponse send(String deviceid, String title, String content, String intent);
}
