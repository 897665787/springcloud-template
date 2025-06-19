package com.company.adminapi.cache;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.company.common.util.JsonUtil;
import com.company.framework.cache.ICache;
import com.company.system.api.feign.SysUserFeign;
import com.company.system.api.response.SysUserResp;

@Component
public class SysUserCache {

    @Autowired
    private ICache cache;
    @Autowired
    private SysUserFeign sysUserFeign;

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

}
