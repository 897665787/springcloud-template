package com.company.tool.push.core.repository;

import com.company.tool.push.core.Constants;
import lombok.Data;
import lombok.experimental.FieldDefaults;

/**
 * 推送信息
 */
@Data
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class PushInfo {
    String pushId;
    Constants.DeviceType deviceType;
    Constants.MessageType messageType;
}
