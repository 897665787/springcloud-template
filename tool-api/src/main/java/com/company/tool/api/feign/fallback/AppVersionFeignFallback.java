package com.company.tool.api.feign.fallback;

import com.company.common.api.Result;
import com.company.tool.api.feign.AppVersionFeign;
import com.company.tool.api.response.AppVersionCheckResp;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

@Component
public class AppVersionFeignFallback implements FallbackFactory<AppVersionFeign> {

    @Override
    public AppVersionFeign create(final Throwable e) {
        return new AppVersionFeign() {

            @Override
            public AppVersionCheckResp check(String appCode, String currentVersion) {
                return Result.onFallbackError();
            }
        };
    }
}
