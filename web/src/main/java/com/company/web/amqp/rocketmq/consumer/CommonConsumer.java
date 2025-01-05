package com.company.web.amqp.rocketmq.consumer;

import com.company.web.amqp.rocketmq.Constants;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

//@Component
@RocketMQMessageListener(
        topic = Constants.QUEUE.COMMON.NAME,
        consumerGroup = "topic-test" + "-common")
@Slf4j
public class CommonConsumer implements RocketMQListener<String> {
    @Override
    public void onMessage(String s) {
        log.info("onMessage: {}", s);
        System.out.printf("onMessage: %s %n", s);
//        ConsumerUtils.handleByStrategy(jsonStrMsg, channel, message);
    }
}

//	@RabbitListener(bindings = @QueueBinding(value = @Queue(value = Constants.QUEUE.COMMON.NAME), exchange = @Exchange(value = Constants.EXCHANGE.DIRECT), key = Constants.QUEUE.COMMON.ROUTING_KEY))
//	public void handle(String jsonStrMsg, Channel channel, Message message) {
//		ConsumerUtils.handleByStrategy(jsonStrMsg, channel, message);
//	}
//}