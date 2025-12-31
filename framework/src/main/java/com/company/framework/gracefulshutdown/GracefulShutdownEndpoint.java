package com.company.framework.gracefulshutdown;

import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class GracefulShutdownEndpoint {

    private final List<ConsumerComponent> consumerComponentList; // 优雅停机

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
