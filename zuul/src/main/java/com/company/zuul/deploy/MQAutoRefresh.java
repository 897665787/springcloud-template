package com.company.zuul.deploy;

import java.util.function.Consumer;

import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Conditional;
import org.springframework.stereotype.Component;

import com.company.zuul.amqp.rabbit.constants.FanoutConstants;
import com.company.zuul.amqp.rabbit.utils.ConsumerUtils;
import com.company.zuul.autoconfigure.RabbitAutoConfiguration.RabbitCondition;
import com.rabbitmq.client.Channel;

import lombok.extern.slf4j.Slf4j;

/**
 * MQ自动刷新注册列表(如果使用了MQ，可以利用MQ广播消息自动刷新)
 * 
 * @author JQ棣
 *
 */
@Slf4j
@Component
@Conditional(RabbitCondition.class)
public class MQAutoRefresh {

	@Value("${spring.application.name}")
	private String applicationName;
	
	@Autowired
	private RefreshHandler refreshHandler;
	
	/**
	 * 临时队列名由fanout.deploy-${spring.application.name}-${spring.cloud.client.ip-address}-${server.port}构成
	 * 每个服务（包括同个服务的集群部署）都要有单独的队列来监听fanout exchange的消息（用于监听其他服务下线或上线事件，重新拉取注册信息）
	 * 
	 * @param msg
	 */
	@RabbitListener(bindings = @QueueBinding(value = @Queue(value = FanoutConstants.PREFIX + "deploy-${spring.application.name}-${spring.cloud.client.ip-address}-${server.port}", durable = "false", autoDelete = "true", exclusive = "true"), exchange = @Exchange(value = FanoutConstants.DEPLOY.EXCHANGE, type = ExchangeTypes.FANOUT, durable = "false", autoDelete = "true")))
	public void handle(String msg, Channel channel, Message message) {
		ConsumerUtils.handleByConsumer(msg, channel, message, new Consumer<String>() {
			@Override
			public void accept(String params) {
				boolean result = refreshHandler.refreshRegistry();
				log.info("#### refresh msg:{},result:{}", params, result);
			}
		});
	}
}
