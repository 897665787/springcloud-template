package com.company.tool.push.core.baidu;


import com.company.tool.push.core.Constants;
import com.company.tool.push.core.PushSender;
import com.company.tool.push.core.SendResponse;
import com.company.tool.push.core.repository.PushInfo;
import com.company.tool.push.core.repository.PushInfoRepository;

public class BaiduPushSender implements PushSender {

    private PushClient client = null;
    private PushInfoRepository repository = null;

    public BaiduPushSender(String apiKey, String secretKey, PushInfoRepository repository) {
        this.client = new PushClient(apiKey, secretKey);
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
        Constants.DeviceType deviceTypeEnum = pushInfo.getDeviceType();

        Integer deviceType = null;// 设备类型，3：Android，4：IOS
        if (deviceTypeEnum == Constants.DeviceType.Android) {
            deviceType = 3;
        } else if (deviceTypeEnum == Constants.DeviceType.iOS) {
            deviceType = 4;
        }

        Constants.MessageType messageTypeEnum = pushInfo.getMessageType();
        Integer messageType = null;// 消息类型,0：透传消息 1：通知 默认值为0
        if (messageTypeEnum != Constants.MessageType.NOTICE) {
            messageType = 1;
        } else if (messageTypeEnum == Constants.MessageType.TRANSPORT) {
            messageType = 0;
        }

        return client.send(channelId, deviceType, title, content, intent, messageType);
    }
}
