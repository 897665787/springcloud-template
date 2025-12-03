package com.company.framework.messagedriven.rocketmq.consumer;

import com.company.framework.messagedriven.constants.HeaderConstants;
import com.company.framework.messagedriven.rocketmq.RocketMQAutoConfiguration;
import com.company.framework.messagedriven.rocketmq.utils.ConsumerUtils;
import com.company.framework.messagedriven.strategy.StrategyConstants;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Conditional;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Map;

@Component
@RocketMQMessageListener(
        topic = "${messagedriven.canal.exchange}",
        consumerGroup = "${messagedriven.canal.queue}"
)
@Slf4j
@Conditional(RocketMQAutoConfiguration.RocketMQCondition.class)
@ConditionalOnProperty(value = "canal.mode", havingValue = "messagedriven")
public class CanalConsumer implements RocketMQListener<MessageExt> {
    @Override
    public void onMessage(MessageExt messageExt) {
        /*
        jsonStrMsg样例：
		{"data":[{"id":"7","nickname":null,"avator":null,"remark":"2111","create_time":"2022-09-10 21:40:06","update_time":"2022-09-10 22:10:10"}],"database":"template","es":1662819010000,"id":7,"isDdl":false,"mysqlType":{"id":"int(11)","nickname":"varchar(32)","avator":"varchar(32)","remark":"varchar(255)","create_time":"datetime","update_time":"datetime"},"old":[{"remark":"211","update_time":"2022-09-10 22:03:26"}],"pkNames":["id"],"sql":"","sqlType":{"id":4,"nickname":12,"avator":12,"remark":12,"create_time":93,"update_time":93},"table":"bu_user_info","ts":1662819010724,"type":"UPDATE"}
		{"data":[{"id":"8","nickname":"2","avator":null,"remark":null,"create_time":"2022-09-10 22:10:19","update_time":"2022-09-10 22:10:19"}],"database":"template","es":1662819019000,"id":8,"isDdl":false,"mysqlType":{"id":"int(11)","nickname":"varchar(32)","avator":"varchar(32)","remark":"varchar(255)","create_time":"datetime","update_time":"datetime"},"old":null,"pkNames":["id"],"sql":"","sqlType":{"id":4,"nickname":12,"avator":12,"remark":12,"create_time":93,"update_time":93},"table":"bu_user_info","ts":1662819019241,"type":"INSERT"}
		{"data":[{"id":"1","nickname":"jiang1","avator":null,"remark":"1111","create_time":"2022-08-14 21:39:28","update_time":"2022-09-10 22:12:45"},{"id":"2","nickname":"221111","avator":null,"remark":"1111","create_time":"2022-09-09 22:22:30","update_time":"2022-09-10 22:12:45"},{"id":"3","nickname":"22211","avator":null,"remark":"1111","create_time":"2022-09-09 22:38:21","update_time":"2022-09-10 22:12:45"}],"database":"template","es":1662819165000,"id":9,"isDdl":false,"mysqlType":{"id":"int(11)","nickname":"varchar(32)","avator":"varchar(32)","remark":"varchar(255)","create_time":"datetime","update_time":"datetime"},"old":[{"remark":null,"update_time":"2022-09-09 22:38:18"},{"remark":null,"update_time":"2022-09-09 22:43:53"},{"remark":null,"update_time":"2022-09-10 21:28:09"}],"pkNames":["id"],"sql":"","sqlType":{"id":4,"nickname":12,"avator":12,"remark":12,"create_time":93,"update_time":93},"table":"bu_user_info","ts":1662819165548,"type":"UPDATE"}
         */
        Map<String, String> properties = messageExt.getProperties();
        String jsonStrMsg = new String(messageExt.getBody(), StandardCharsets.UTF_8);

        properties.put(HeaderConstants.HEADER_STRATEGY_NAME, StrategyConstants.CANAL_STRATEGY);
        properties.put(HeaderConstants.HEADER_PARAMS_CLASS, Map.class.getName());
        ConsumerUtils.handleByStrategy(jsonStrMsg, properties);
    }
}
