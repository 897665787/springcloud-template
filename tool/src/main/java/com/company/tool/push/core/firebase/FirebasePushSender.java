package com.company.tool.push.core.firebase;


import com.company.tool.push.core.Constants;
import com.company.tool.push.core.PushSender;
import com.company.tool.push.core.SendResponse;
import com.company.tool.push.core.repository.DeviceInfo;
import com.company.tool.push.core.repository.DeviceRepository;

public class FirebasePushSender implements PushSender {

	private PushClient client = null;
	private DeviceRepository repository = null;

	public FirebasePushSender(String accountFilePath, DeviceRepository repository) {
		this.client = new PushClient(accountFilePath);
		this.repository = repository;
	}

	@Override
	public void bindDevice(String deviceid, String channelId, Constants.DeviceType deviceType) {
		DeviceInfo deviceInfo = new DeviceInfo();
		deviceInfo.setChannelId(channelId);
		deviceInfo.setDeviceType(deviceType);
		repository.bindDeviceInfo(deviceid, deviceInfo);
	}

	@Override
	public SendResponse send(String deviceid, String title, String content, String intent) {
		DeviceInfo deviceInfo = repository.getByDeviceid(deviceid);
		String channelId = deviceInfo.getChannelId();
		return client.send(channelId, title, content, intent);
	}
}
