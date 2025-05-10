package com.company.tool.push.core.jiguang;


import com.company.tool.push.core.Constants;
import com.company.tool.push.core.PushSender;
import com.company.tool.push.core.SendResponse;
import com.company.tool.push.core.repository.PushInfo;
import com.company.tool.push.core.repository.PushInfoRepository;

public class JiguangPushSender implements PushSender {

	private PushClient client = null;
	private PushInfoRepository repository = null;

	public JiguangPushSender(String apiKey, String secretKey, PushInfoRepository repository) {
		this.client = new PushClient(apiKey, secretKey);
		this.repository = repository;
	}

	@Override
	public void bindDevice(String deviceid, String registrationId, Constants.DeviceType deviceType) {
		PushInfo pushInfo = new PushInfo();
		pushInfo.setPushId(registrationId);
		pushInfo.setDeviceType(deviceType);
		repository.bindDeviceInfo(deviceid, pushInfo);
	}

	@Override
	public SendResponse send(String deviceid, String title, String content, String intent) {
		PushInfo pushInfo = repository.getByDeviceid(deviceid);
		String registrationId = pushInfo.getPushId();
		return client.send(registrationId, title, content, intent);
	}
}
