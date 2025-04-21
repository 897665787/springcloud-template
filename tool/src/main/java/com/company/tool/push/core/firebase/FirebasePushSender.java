package com.company.tool.push.core.firebase;


import com.company.tool.push.core.Constants;
import com.company.tool.push.core.PushSender;
import com.company.tool.push.core.SendResponse;
import com.company.tool.push.core.repository.PushInfo;
import com.company.tool.push.core.repository.PushInfoRepository;

public class FirebasePushSender implements PushSender {

	private PushClient client = null;
	private PushInfoRepository repository = null;

	public FirebasePushSender(String accountFilePath, PushInfoRepository repository) {
		this.client = new PushClient(accountFilePath);
		this.repository = repository;
	}

	@Override
	public void bindDevice(String deviceid, String channelId, Constants.DeviceType deviceType) {
		PushInfo pushInfo = new PushInfo();
		pushInfo.setPushId(channelId);
		pushInfo.setDeviceType(deviceType);
		repository.bindDeviceInfo(deviceid, pushInfo);
	}

	@Override
	public SendResponse send(String deviceid, String title, String content, String intent) {
		PushInfo pushInfo = repository.getByDeviceid(deviceid);
		String channelId = pushInfo.getPushId();
		return client.send(channelId, title, content, intent);
	}
}
