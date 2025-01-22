package com.company.admin.mapper.user;

import com.company.admin.entity.user.User;
import com.company.admin.entity.user.UserAddress;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author JQ棣
 * @date 2018/10/27
 */
public interface UserDao {

    int save(User user);

    int remove(User user);

    User get(User user);

    int update(User user);

    List<User> list(User user);

    Long count(User user);

    List<UserAddress> userAddressList(UserAddress userAddress);

    @Select("SELECT * FROM sc_user WHERE id=#{userId} FOR UPDATE")
    User getAndLock(String userId);

    @Update("UPDATE sc_user SET ios_wallet = ios_wallet + #{fee},update_time = NOW() WHERE id = #{userId}")
    int updateIOSWallet(@Param("userId") String userId, @Param("fee") BigDecimal fee);

    @Update("UPDATE sc_user SET android_wallet = android_wallet + #{fee},update_time = NOW() WHERE id = #{userId}")
    void updateAndroidWallet(@Param("userId") String userId, @Param("fee") BigDecimal fee);

    @Update("UPDATE sc_user SET vip = #{vip}, vip_expire = #{vipExpire},update_time = NOW() WHERE id = #{userId}")
    void updateVip(@Param("userId") String userId, @Param("vip") Integer vip, @Param("vipExpire") Date vipExpire);

    List<User> listInvitedUser(User user);

    Long countInvitedUser(User user);

    @Select("SELECT * FROM sc_user WHERE mobile=#{mobile}")
    User getByMobile(String mobile);
}
