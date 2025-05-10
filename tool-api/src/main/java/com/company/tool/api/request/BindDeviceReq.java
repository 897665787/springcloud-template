package com.company.tool.api.request;

import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class BindDeviceReq {
    String deviceid;// 设备ID
    String pushId;// 推送ID（推送平台唯一）
}
