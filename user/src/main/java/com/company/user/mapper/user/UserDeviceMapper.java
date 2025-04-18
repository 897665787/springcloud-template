package com.company.user.mapper.user;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.company.user.entity.UserSource;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

public interface UserDeviceMapper extends BaseMapper<UserSource> {

    @Insert("insert into user_device(user_id,deviceid) values (#{userId},#{deviceid})"
            + " ON DUPLICATE KEY UPDATE"
            + " update_time = now()")
    int saveOrUpdate(@Param("userId") Integer userId, @Param("deviceid") String deviceid);

    /**
     * 根据设备ID查询用户ID
     *
     * @param deviceid
     * @return
     */
    @Select("select user_id from user_device where deviceid = #{deviceid} order by update_time desc limit 1")
    Integer getUserIdByDeviceid(@Param("deviceid") String deviceid);

    /**
     * 根据用户ID查询设备ID
     *
     * @param userId
     * @return
     */
    @Select("select deviceid from user_device where user_id = #{userId} order by update_time desc limit 1")
    String getDeviceidByUserId(@Param("userId") Integer userId);
}