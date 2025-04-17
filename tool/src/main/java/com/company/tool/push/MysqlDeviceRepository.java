package com.company.tool.push;

import com.company.tool.push.core.Constants;
import com.company.tool.push.core.repository.DeviceInfo;
import com.company.tool.push.core.repository.DeviceRepository;

/**
 * mysql设备仓库
 *
 * @author candi.jiang
 */
public class MysqlDeviceRepository implements DeviceRepository {

    @Override
    public void bindDeviceInfo(String deviceid, DeviceInfo deviceInfo) {
        String channelId = deviceInfo.getChannelId();
        String code = deviceInfo.getDeviceType().getCode();

    }

    @Override
    public DeviceInfo getByDeviceid(String deviceid) {
        DeviceInfo deviceInfo = new DeviceInfo();
        deviceInfo.setChannelId("");
        Constants.DeviceType deviceType = Constants.DeviceType.of("");
        deviceInfo.setDeviceType(deviceType);

        Constants.MessageType messageType = Constants.MessageType.NOTICE;
        // 根据用户在线状态来判断发送通知消息还是透传消息
        if (true) {
            messageType = Constants.MessageType.TRANSPORT;
        }
        deviceInfo.setMessageType(messageType);
        return deviceInfo;
    }
}
