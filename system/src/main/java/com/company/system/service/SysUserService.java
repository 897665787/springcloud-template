package com.company.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.company.system.entity.SysUser;
import com.company.system.mapper.SysUserMapper;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SysUserService extends ServiceImpl<SysUserMapper, SysUser> implements IService<SysUser> {

	public SysUser getByAccount(String account) {
		return baseMapper.selectByAccount(account);
	}

	public boolean logicRemoveBatchByIds(List<Integer> idList) {
		return baseMapper.logicRemoveBatchByIds(idList);
	}

	public List<SysUser> selectByIds(Collection<Integer> ids) {
		if (CollectionUtils.isEmpty(ids)) {
			return Collections.emptyList();
		}
		ids = ids.stream().collect(Collectors.toSet());// 去重
		return baseMapper.selectByIds(ids);
	}
}