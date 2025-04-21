package com.company.tool.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.company.tool.entity.PushDeviceBind;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

public interface PushDeviceBindMapper extends BaseMapper<PushDeviceBind> {

    @Select("select * from push_device_bind where deviceid = #{deviceid}")
    PushDeviceBind selectByDeviceid(@Param("deviceid") String deviceid);
}