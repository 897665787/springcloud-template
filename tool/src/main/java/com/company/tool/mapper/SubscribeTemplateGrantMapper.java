package com.company.tool.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.company.tool.entity.SubscribeTemplateGrant;

public interface SubscribeTemplateGrantMapper extends BaseMapper<SubscribeTemplateGrant> {

	@Insert("INSERT INTO subscribe_template_grant(openid,template_code,total_num,use_num)"
			+ " VALUES (#{openid}, #{templateCode}, 1, 0)"
			+ " ON DUPLICATE KEY UPDATE"
			+ " total_num = total_num + 1")
	Integer incrTotalNum(@Param("openid") String openid, @Param("templateCode") String templateCode);

	@Update("update subscribe_template_grant set use_num = use_num + 1 where openid = #{openid} and template_code = #{templateCode} and use_num < total_num")
	Integer incrUseNum(@Param("openid") String openid, @Param("templateCode") String templateCode);
	
	@Update("update subscribe_template_grant set use_num = use_num - 1 where openid = #{openid} and template_code = #{templateCode}")
	Integer returnUseNum(@Param("openid") String openid, @Param("templateCode") String templateCode);
}