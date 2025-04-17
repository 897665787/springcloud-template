package com.company.tool.push.core.baidu;


import com.company.tool.push.core.*;
import com.company.tool.push.core.repository.DeviceInfo;
import com.company.tool.push.core.repository.DeviceRepository;

public class BaiduPushSender implements PushSender {

	private PushClient client = null;
	private DeviceRepository repository = null;

	public BaiduPushSender(String apiKey, String secretKey, DeviceRepository repository) {
		this.client = new PushClient(apiKey, secretKey);
		this.repository = repository;
	}

	@Override
	public void bindDevice(String deviceid, String channelId, Constants.DeviceType deviceType) {
		DeviceInfo deviceInfo = new DeviceInfo();
		deviceInfo.setChannelId(channelId);
		deviceInfo.setDeviceType(deviceType);
		repository.bindDeviceInfo(deviceid, deviceInfo);
	}

//	@Override
//	public String channel() {
//		return Constants.Channel.BAIDU;
//	}

	@Override
	public SendResponse send(String deviceid, String title, String content, String intent) {
		DeviceInfo deviceInfo = repository.getByDeviceid(deviceid);
		String channelId = deviceInfo.getChannelId();
		Constants.DeviceType deviceTypeEnum = deviceInfo.getDeviceType();

		Integer deviceType = null;// 设备类型，3：Android，4：IOS
		if (deviceTypeEnum == Constants.DeviceType.Android) {
			deviceType = 3;
		} else if (deviceTypeEnum == Constants.DeviceType.iOS) {
			deviceType = 4;
		}

		Integer messageType = null;

		return client.send(channelId, deviceType, title, content, intent, messageType);
	}
}
