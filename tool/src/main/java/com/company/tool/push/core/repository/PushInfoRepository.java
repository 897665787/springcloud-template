package com.company.tool.push.core.repository;

/**
 * 推送信息仓库
 */
public interface PushInfoRepository {
    /**
     * 绑定设备ID与推送信息
     */
    void bindDeviceInfo(String deviceid, PushInfo pushInfo);

    /**
     * 根据设备ID查询推送信息
     */
    PushInfo getByDeviceid(String deviceid);
}
