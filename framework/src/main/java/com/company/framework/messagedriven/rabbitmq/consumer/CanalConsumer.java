package com.company.framework.messagedriven.rabbitmq.consumer;

import com.company.framework.messagedriven.constants.HeaderConstants;
import com.company.framework.messagedriven.rabbitmq.RabbitMQAutoConfiguration;
import com.company.framework.messagedriven.rabbitmq.utils.ConsumerUtils;
import com.company.framework.messagedriven.strategy.StrategyConstants;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Conditional;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component
@Conditional(RabbitMQAutoConfiguration.RabbitMQCondition.class)
@ConditionalOnProperty(value = "canal.mode", havingValue = "messagedriven")
public class CanalConsumer {

	@RabbitListener(
            bindings = @QueueBinding(value = @Queue(value = "${messagedriven.canal.queue}", durable = "false", autoDelete = "true"),
                    exchange = @Exchange(value = "${messagedriven.canal.exchange}", type = ExchangeTypes.FANOUT, durable = "false", autoDelete = "true")))
    public void handle(String jsonStrMsg, Channel channel, Message message) {
        /*
        jsonStrMsg样例：
		{"data":[{"id":"7","nickname":null,"avator":null,"remark":"2111","create_time":"2022-09-10 21:40:06","update_time":"2022-09-10 22:10:10"}],"database":"template","es":1662819010000,"id":7,"isDdl":false,"mysqlType":{"id":"int(11)","nickname":"varchar(32)","avator":"varchar(32)","remark":"varchar(255)","create_time":"datetime","update_time":"datetime"},"old":[{"remark":"211","update_time":"2022-09-10 22:03:26"}],"pkNames":["id"],"sql":"","sqlType":{"id":4,"nickname":12,"avator":12,"remark":12,"create_time":93,"update_time":93},"table":"user_info","ts":1662819010724,"type":"UPDATE"}
		{"data":[{"id":"8","nickname":"2","avator":null,"remark":null,"create_time":"2022-09-10 22:10:19","update_time":"2022-09-10 22:10:19"}],"database":"template","es":1662819019000,"id":8,"isDdl":false,"mysqlType":{"id":"int(11)","nickname":"varchar(32)","avator":"varchar(32)","remark":"varchar(255)","create_time":"datetime","update_time":"datetime"},"old":null,"pkNames":["id"],"sql":"","sqlType":{"id":4,"nickname":12,"avator":12,"remark":12,"create_time":93,"update_time":93},"table":"user_info","ts":1662819019241,"type":"INSERT"}
		{"data":[{"id":"1","nickname":"jiang1","avator":null,"remark":"1111","create_time":"2022-08-14 21:39:28","update_time":"2022-09-10 22:12:45"},{"id":"2","nickname":"221111","avator":null,"remark":"1111","create_time":"2022-09-09 22:22:30","update_time":"2022-09-10 22:12:45"},{"id":"3","nickname":"22211","avator":null,"remark":"1111","create_time":"2022-09-09 22:38:21","update_time":"2022-09-10 22:12:45"}],"database":"template","es":1662819165000,"id":9,"isDdl":false,"mysqlType":{"id":"int(11)","nickname":"varchar(32)","avator":"varchar(32)","remark":"varchar(255)","create_time":"datetime","update_time":"datetime"},"old":[{"remark":null,"update_time":"2022-09-09 22:38:18"},{"remark":null,"update_time":"2022-09-09 22:43:53"},{"remark":null,"update_time":"2022-09-10 21:28:09"}],"pkNames":["id"],"sql":"","sqlType":{"id":4,"nickname":12,"avator":12,"remark":12,"create_time":93,"update_time":93},"table":"user_info","ts":1662819165548,"type":"UPDATE"}
         */
        message.getMessageProperties().setHeader(HeaderConstants.HEADER_STRATEGY_NAME, StrategyConstants.CANAL_STRATEGY);
        message.getMessageProperties().setHeader(HeaderConstants.HEADER_PARAMS_CLASS, Map.class.getName());
        ConsumerUtils.handleByStrategy(jsonStrMsg, channel, message);
	}
}
