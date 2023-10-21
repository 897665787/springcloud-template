package com.company.tool.service;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.company.tool.entity.SubscribeTemplateGrant;
import com.company.tool.mapper.SubscribeTemplateGrantMapper;

@Service
public class SubscribeTemplateGrantService extends ServiceImpl<SubscribeTemplateGrantMapper, SubscribeTemplateGrant> {

	public void grant(String openid, String templateCode) {
		baseMapper.incrTotalNum(openid, templateCode);
	}

	public boolean hasGrantByOpenidTemplateCode(String openid, String templateCode) {
		Integer affect = baseMapper.incrUseNum(openid, templateCode);
		return affect > 0;
	}
	
	public boolean incrUseNum(String openid, String templateCode) {
		Integer affect = baseMapper.incrUseNum(openid, templateCode);
		return affect > 0;
	}

	public void returnUseNum(String openid, String templateCode) {
		baseMapper.returnUseNum(openid, templateCode);
	}
	
	public void zeroNum(String openid, String templateCode) {
		baseMapper.zeroNum(openid, templateCode);
	}

}
