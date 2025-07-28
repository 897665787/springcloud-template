package com.company.framework.deploy;

import com.company.common.api.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 部署相关接口（用于优雅发版）
 *
 * @author JQ棣
 */
@RestController
public class DeployController {

    @Autowired(required = false)
    private List<ConsumerComponent> consumerComponentList; // 优雅停机

    /**
     * 下线
     *
     * @return
     */
    @GetMapping("/offline")
    public Result<?> offline() {
        if (consumerComponentList == null) {
            return Result.success();
        }
        consumerComponentList.forEach(ConsumerComponent::offline);
        return Result.success();
    }

    @Autowired(required = false)
    private RefreshHandler refreshHandler; // 优雅停机

    /**
     * 下线
     *
     * @return
     */
    @GetMapping("/refresh")
    public Result<?> refresh() {
        refreshHandler.refresh("template-order");
        return Result.success();
    }

}
