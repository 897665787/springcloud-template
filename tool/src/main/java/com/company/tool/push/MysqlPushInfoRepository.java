package com.company.tool.push;

import com.company.tool.entity.PushDeviceBind;
import com.company.tool.mapper.PushDeviceBindMapper;
import com.company.tool.push.core.Constants;
import com.company.tool.push.core.repository.PushInfo;
import com.company.tool.push.core.repository.PushInfoRepository;
import com.company.user.api.feign.DeviceFeign;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * mysql设备仓库
 */
@Component
public class MysqlPushInfoRepository implements PushInfoRepository {

    @Autowired
    private PushDeviceBindMapper pushDeviceBindMapper;
    @Autowired
    private DeviceFeign deviceFeign;

    @Override
    public void bindDeviceInfo(String deviceid, PushInfo pushInfo) {
        String pushId = pushInfo.getPushId();
        String code = pushInfo.getDeviceType().getCode();

        PushDeviceBind pushDeviceBind = new PushDeviceBind();
        pushDeviceBind.setDeviceid(deviceid);
        pushDeviceBind.setPushId(pushId);
        pushDeviceBind.setDeviceType(code);
        pushDeviceBindMapper.insertOrUpdate(pushDeviceBind);
    }

    @Override
    public PushInfo getByDeviceid(String deviceid) {
        PushDeviceBind pushDeviceBind = pushDeviceBindMapper.selectByDeviceid(deviceid);
        PushInfo pushInfo = new PushInfo();
        pushInfo.setPushId(pushDeviceBind.getPushId());
        Constants.DeviceType deviceType = Constants.DeviceType.of(pushDeviceBind.getDeviceType());
        pushInfo.setDeviceType(deviceType);

        Constants.MessageType messageType = Constants.MessageType.NOTICE;
        // 根据用户在线状态来判断发送通知消息还是透传消息
        Boolean isOnline = deviceFeign.isOnline(deviceid).dataOrThrow();
        if (isOnline) {
            messageType = Constants.MessageType.TRANSPORT;
        }
        pushInfo.setMessageType(messageType);
        return pushInfo;
    }
}
