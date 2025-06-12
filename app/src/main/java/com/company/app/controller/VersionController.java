package com.company.app.controller;

import com.company.common.api.Result;
import com.company.tool.api.feign.AppVersionFeign;
import com.company.tool.api.response.AppVersionCheckResp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 版本
 *
 * @author JQ棣
 */
@RestController
@RequestMapping("/version")
public class VersionController {

    @Autowired
    private AppVersionFeign appVersionFeign;

    /**
     * 检查
     */
    @GetMapping("/check")
    public Result<AppVersionCheckResp> check(String appCode, String version) {
        return appVersionFeign.check(appCode, version);
    }
}
