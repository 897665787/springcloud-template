package com.company.user.cache;

import com.company.framework.cache.ICache;
import com.company.framework.util.JsonUtil;
import com.company.user.api.response.UserInfoResp;
import com.company.user.entity.UserInfo;
import com.company.user.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;

@Component
public class UserInfoCache {
    private static final String KEY_PATTERN = "user:userinfo:%s";

    @Autowired
    private ICache cache;
    @Autowired
    private UserInfoService userInfoService;
    @Autowired
    private CacheManager cacheManager;

    public UserInfoResp getById(Integer id) {
        String key = String.format(KEY_PATTERN, id);
        Cache cache1 = cacheManager.getCache(key);
        UserInfoResp userInfoResp = cache1.get(key, () -> {
            UserInfo userInfo = userInfoService.getById(id);
            if (userInfo == null) {
                userInfo = new UserInfo();
            }
            return JsonUtil.toEntity(JsonUtil.toJsonString(userInfo), UserInfoResp.class);
        });

        return cache.get(key, () -> {
            UserInfo userInfo = userInfoService.getById(id);
            if (userInfo == null) {
                userInfo = new UserInfo();
            }
            return JsonUtil.toJsonString(userInfo);
        }, UserInfoResp.class);
    }

    public void del(Integer id) {
        String key = String.format(KEY_PATTERN, id);
        cache.del(key);
    }
}
