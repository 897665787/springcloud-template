package com.company.user.mapper.user;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.company.user.api.enums.UserOauthEnum;
import com.company.user.entity.UserOauth;

public interface UserOauthMapper extends BaseMapper<UserOauth> {

	@Insert("insert into bu_user_oauth(user_id,identity_type,identifier,certificate) values (#{userId},#{identityType.code},#{identifier},#{certificate})"
			+ " on duplicate key update"
			+ " user_id = #{userId},identity_type = #{identityType.code},identifier = #{identifier},certificate = #{certificate}")
	int bindOauth(@Param("userId") Integer userId, @Param("identityType") UserOauthEnum.IdentityType identityType,
			@Param("identifier") String identifier, @Param("certificate") String certificate);
	
	@Select("select * from bu_user_oauth where identity_type = #{identityType.code} and identifier = #{identifier}")
	UserOauth selectByIdentityTypeIdentifier(@Param("identityType") UserOauthEnum.IdentityType identityType,
			@Param("identifier") String identifier);
	
	@Select("select * from bu_user_oauth where user_id = #{userId} and identity_type = #{identityType.code}")
	UserOauth selectByUserIdIdentityType(@Param("userId") Integer userId,
			@Param("identityType") UserOauthEnum.IdentityType identityType);
}
