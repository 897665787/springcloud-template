
package com.company.framework.messagedriven.rocketmq.gracefulshutdown;

import com.company.framework.deploy.ConsumerComponent;
import com.company.framework.messagedriven.rabbitmq.RabbitMQAutoConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.support.DefaultRocketMQListenerContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Conditional;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Rocketmq 消费者下线
 *
 * @author JQ棣
 */
@Slf4j
@Component
@Conditional(RabbitMQAutoConfiguration.RabbitMQCondition.class)
public class RocketmqConsumerComponent implements ConsumerComponent {

    @Autowired
    private List<DefaultRocketMQListenerContainer> defaultRocketMQListenerContainerList;

    @Override
    public void offline() {
        if (defaultRocketMQListenerContainerList == null) {
            return;
        }
        // 下线RocketMQ消费者
        defaultRocketMQListenerContainerList.forEach(DefaultRocketMQListenerContainer::destroy);
        log.info("RocketMQ消费者已下线");
    }
}
