package com.company.adminapi.service;

/**
 * 缓存刷新服务
 *
 * @author JQ棣
 * @email 897665787@qq.com
 * @date 2026/10/22 10:30
 * @Copyright Copyright (c) Speediance Inc. All Rights Reserved.
 **/

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

/**
 * 缓存刷新服务
 * <p>
 * 字典、参数配置修改后调用，清空本地/Redis 缓存，使下一次读取回源数据库。
 */
@Slf4j
@Service
public class CacheRefreshService {

	@Autowired
	private CacheManager cacheManager;

	/**
	 * 清空指定缓存
	 */
	public boolean refresh(String cacheName) {
		Cache cache = cacheManager.getCache(cacheName);
		if (cache == null) {
			log.info("缓存不存在,cacheName:{}", cacheName);
			return true;
		}
		cache.clear();
		return true;
	}
}
