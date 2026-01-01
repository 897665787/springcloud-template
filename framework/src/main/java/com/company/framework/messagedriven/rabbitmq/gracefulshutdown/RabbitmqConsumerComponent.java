
package com.company.framework.messagedriven.rabbitmq.gracefulshutdown;

import com.company.framework.gracefulshutdown.ConsumerComponent;
import com.company.framework.messagedriven.rabbitmq.RabbitMQAutoConfiguration;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.listener.RabbitListenerEndpointRegistry;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Conditional;
import org.springframework.stereotype.Component;

/**
 * Rabbitmq 消费者下线
 *
 * @author JQ棣
 */
@Slf4j
@Component
@Conditional(RabbitMQAutoConfiguration.RabbitMQCondition.class)
@RequiredArgsConstructor
public class RabbitmqConsumerComponent implements ConsumerComponent {

    private final RabbitListenerEndpointRegistry rabbitListenerEndpointRegistry;

    @Override
    public void preStop() {
        // 下线RabbitMQ消费者
        rabbitListenerEndpointRegistry.stop();
        log.info("RabbitMQ消费者已下线");
    }
}
