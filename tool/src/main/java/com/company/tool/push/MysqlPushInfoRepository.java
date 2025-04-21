package com.company.tool.push;

import com.company.tool.push.core.Constants;
import com.company.tool.push.core.repository.PushInfo;
import com.company.tool.push.core.repository.PushInfoRepository;

/**
 * mysql设备仓库
 *
 * @author candi.jiang
 */
public class MysqlPushInfoRepository implements PushInfoRepository {

    @Override
    public void bindDeviceInfo(String deviceid, PushInfo pushInfo) {
        String pushId = pushInfo.getPushId();
        String code = pushInfo.getDeviceType().getCode();

    }

    @Override
    public PushInfo getByDeviceid(String deviceid) {
        PushInfo pushInfo = new PushInfo();
        pushInfo.setPushId("");
        Constants.DeviceType deviceType = Constants.DeviceType.of("");
        pushInfo.setDeviceType(deviceType);

        Constants.MessageType messageType = Constants.MessageType.NOTICE;
        // 根据用户在线状态来判断发送通知消息还是透传消息
        if (true) {
            messageType = Constants.MessageType.TRANSPORT;
        }
        pushInfo.setMessageType(messageType);
        return pushInfo;
    }
}
