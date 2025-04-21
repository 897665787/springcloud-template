package com.company.tool.api.request;

import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.HashMap;
import java.util.Map;

@Data
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class BindDeviceReq {
    String deviceid;// 设备ID
    String channel;// 发送渠道(log:打印日志（测试）,ali:阿里云,tencent:腾讯云)
    String pushId;// 推送ID（推送平台唯一）
}
