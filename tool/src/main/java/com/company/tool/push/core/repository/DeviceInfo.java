package com.company.tool.push.core.repository;

import com.company.tool.push.core.Constants;
import lombok.Data;
import lombok.experimental.FieldDefaults;

/**
 * 设备信息
 *
 * @author candi.jiang
 */
@Data
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class DeviceInfo {
    String channelId;
    Constants.DeviceType deviceType;
    Constants.MessageType messageType;
}
