package com.company.user.rabbitmq.consumer;

import java.io.IOException;
import java.util.function.Consumer;

import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.context.annotation.Conditional;
import org.springframework.stereotype.Component;

import com.company.common.exception.BusinessException;
import com.company.common.util.JsonUtil;
import com.company.common.util.MdcUtil;
import com.company.framework.amqp.rabbit.constants.FanoutConstants;
import com.company.framework.autoconfigure.RabbitAutoConfiguration.RabbitCondition;
import com.rabbitmq.client.Channel;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@Conditional(RabbitCondition.class)
public class CanalConsumer {

	@RabbitListener(bindings = @QueueBinding(value = @Queue(value = FanoutConstants.CANAL.USER_QUEUE, durable = "false", autoDelete = "true"), exchange = @Exchange(value = FanoutConstants.CANAL.EXCHANGE, type = ExchangeTypes.FANOUT, durable = "false", autoDelete = "true")))
	public void user(byte[] jsonByteMsg, Channel channel, Message message) {
//		{"data":[{"id":"7","nickname":null,"avator":null,"remark":"2111","create_time":"2022-09-10 21:40:06","update_time":"2022-09-10 22:10:10"}],"database":"template","es":1662819010000,"id":7,"isDdl":false,"mysqlType":{"id":"int(11)","nickname":"varchar(32)","avator":"varchar(32)","remark":"varchar(255)","create_time":"datetime","update_time":"datetime"},"old":[{"remark":"211","update_time":"2022-09-10 22:03:26"}],"pkNames":["id"],"sql":"","sqlType":{"id":4,"nickname":12,"avator":12,"remark":12,"create_time":93,"update_time":93},"table":"bu_user_info","ts":1662819010724,"type":"UPDATE"}
//		{"data":[{"id":"8","nickname":"2","avator":null,"remark":null,"create_time":"2022-09-10 22:10:19","update_time":"2022-09-10 22:10:19"}],"database":"template","es":1662819019000,"id":8,"isDdl":false,"mysqlType":{"id":"int(11)","nickname":"varchar(32)","avator":"varchar(32)","remark":"varchar(255)","create_time":"datetime","update_time":"datetime"},"old":null,"pkNames":["id"],"sql":"","sqlType":{"id":4,"nickname":12,"avator":12,"remark":12,"create_time":93,"update_time":93},"table":"bu_user_info","ts":1662819019241,"type":"INSERT"}
//		{"data":[{"id":"1","nickname":"jiang1","avator":null,"remark":"1111","create_time":"2022-08-14 21:39:28","update_time":"2022-09-10 22:12:45"},{"id":"2","nickname":"221111","avator":null,"remark":"1111","create_time":"2022-09-09 22:22:30","update_time":"2022-09-10 22:12:45"},{"id":"3","nickname":"22211","avator":null,"remark":"1111","create_time":"2022-09-09 22:38:21","update_time":"2022-09-10 22:12:45"}],"database":"template","es":1662819165000,"id":9,"isDdl":false,"mysqlType":{"id":"int(11)","nickname":"varchar(32)","avator":"varchar(32)","remark":"varchar(255)","create_time":"datetime","update_time":"datetime"},"old":[{"remark":null,"update_time":"2022-09-09 22:38:18"},{"remark":null,"update_time":"2022-09-09 22:43:53"},{"remark":null,"update_time":"2022-09-10 21:28:09"}],"pkNames":["id"],"sql":"","sqlType":{"id":4,"nickname":12,"avator":12,"remark":12,"create_time":93,"update_time":93},"table":"bu_user_info","ts":1662819165548,"type":"UPDATE"}
		handleByConsumer(jsonByteMsg, channel, message, new Consumer<String>() {
			@Override
			public void accept(String params) {
				log.info("user params:{}", JsonUtil.toJsonString(params));
			}
		});
	}

	private static void handleByConsumer(byte[] jsonByteMsg, Channel channel, Message message, Consumer<String> consumer) {
		try {
			if (jsonByteMsg == null) {
				log.info("jsonByteMsg is null");
				basicAck(channel, message);
				return;
			}
			String jsonStrMsg = new String(jsonByteMsg);
			
			long start = System.currentTimeMillis();
			try {
				MessageProperties messageProperties = message.getMessageProperties();
				MdcUtil.put(messageProperties.getMessageId());
				log.info("jsonStrMsg:{}", jsonStrMsg);
				consumer.accept(jsonStrMsg);
			} catch (BusinessException e) {
				// 业务异常一般是校验不通过，可以当做成功处理
				log.warn("BusinessException code:{},message:{}", e.getCode(), e.getMessage());
			} catch (Exception e) {
				log.error("accept error", e);
			} finally {
				log.info("耗时:{}ms", System.currentTimeMillis() - start);
			}
			basicAck(channel, message);
		} finally {
			MdcUtil.remove();
		}
	}
	private static void basicAck(Channel channel, Message message) {
		try {
			channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
		} catch (IOException e) {
			log.error("basicAck error", e);
		}
	}
}