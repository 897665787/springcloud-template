package com.company.adminapi.cache;

import com.company.framework.cache.ICache;
import com.company.framework.util.JsonUtil;
import com.company.system.api.feign.SysUserFeign;
import com.company.system.api.response.SysUserResp;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SysUserCache {

    private final ICache cache;
    private final SysUserFeign sysUserFeign;

    public SysUserResp getById(Integer id) {
        String key = String.format("admin:sysuser:%s", id);
        return cache.get(key, () -> {
            SysUserResp sysUserResp = sysUserFeign.getById(id);
            if (sysUserResp == null) {
                sysUserResp = new SysUserResp();
            }
            return JsonUtil.toJsonString(sysUserResp);
        }, SysUserResp.class);
    }

    @Cacheable(value = "admin:sysuser", key = "#id")
    public SysUserResp getById2(Integer id) {
        return sysUserFeign.getById(id);
    }
}
