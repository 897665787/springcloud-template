package com.company.framework.gracefulshutdown;

import com.company.common.api.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 优雅停机端点
 *
 * @author JQ棣
 */
@RestController
public class GracefulShutdownEndpoint {

    @Autowired(required = false)
    private List<ConsumerComponent> consumerComponentList; // 优雅停机

    /**
     * 预停机
     *
     * @return
     */
    @GetMapping("/prestop")
    public Result<?> preStop() {
        if (consumerComponentList == null) {
            return Result.success();
        }
        consumerComponentList.forEach(ConsumerComponent::preStop);
        return Result.success();
    }

}
