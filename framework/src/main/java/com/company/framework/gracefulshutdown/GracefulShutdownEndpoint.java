package com.company.framework.gracefulshutdown;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 优雅停机端点
 *
 * @author JQ棣
 */
@RestController
@RequestMapping("/gracefulshutdown")
public class GracefulShutdownEndpoint {

    @Autowired(required = false)
    private List<ConsumerComponent> consumerComponentList; // 优雅停机

    /**
     * 预停机
     *
     * @return
     */
    @GetMapping("/prestop")
    public String preStop() {
        if (consumerComponentList == null) {
            return "OK";
        }
        consumerComponentList.forEach(ConsumerComponent::preStop);
        return "OK";
    }

}
