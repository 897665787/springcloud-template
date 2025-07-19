package com.company.user.mapper.user;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.company.user.entity.UserDevice;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDateTime;

public interface UserDeviceMapper extends BaseMapper<UserDevice> {

    @Insert("insert into user_device(user_id,deviceid,last_login_time) values (#{userId},#{deviceid},#{lastLoginTime})"
            + " ON DUPLICATE KEY UPDATE"
            + " last_login_time = #{lastLoginTime}")
    int saveOrUpdateLogin(@Param("userId") Integer userId, @Param("deviceid") String deviceid,
                     @Param("lastLoginTime") LocalDateTime lastLoginTime);

    @Insert("insert into user_device(user_id,deviceid,last_logout_time) values (#{userId},#{deviceid},#{lastLogoutTime})"
            + " ON DUPLICATE KEY UPDATE"
            + " last_logout_time = #{lastLogoutTime}")
    int saveOrUpdateLogout(@Param("userId") Integer userId, @Param("deviceid") String deviceid,
                     @Param("lastLogoutTime") LocalDateTime lastLogoutTime);

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