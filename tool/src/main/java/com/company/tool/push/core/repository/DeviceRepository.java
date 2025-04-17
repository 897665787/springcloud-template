package com.company.tool.push.core.repository;

/**
 * 设备仓库
 *
 * @author candi.jiang
 */
public interface DeviceRepository {
    /**
     * 绑定设备信息
     */
    void bindDeviceInfo(String deviceid, DeviceInfo deviceInfo);

    /**
     * 根据设备ID查询设备信息
     */
    DeviceInfo getByDeviceid(String deviceid);
}
