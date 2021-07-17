package com.company.framework.deploy;

import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

/**
 * MQ自动刷新注册列表(如果使用了MQ，可以利用MQ广播消息自动刷新)
 * 
 * @author Candi
 *
 */
@Slf4j
@Component
@ConditionalOnProperty(prefix = "template.enable", name = "rabbitmq", havingValue = "true", matchIfMissing = false)
public class MQAutoRefresh {
	public static final String EXCHANGE = "deploy";

	@Value("${spring.application.name}")
	private String applicationName;
	
	@Autowired(required = false)
    @Qualifier("rabbitTemplate")
    private RabbitTemplate rabbitTemplate;
	
	@Autowired
	private RefreshHandler refreshHandler;

	/**
	 * 往MQ发送一条广播消息，通知其他服务刷新注册服务列表
	 */
	public void send(String msg) {
		String message = applicationName + "->" + msg;
		log.info("#### send,msg:{}", message);
		if (rabbitTemplate == null) {
			log.info("rabbitTemplate is null");
			return;
		}
		this.rabbitTemplate.convertAndSend(MQAutoRefresh.EXCHANGE, "", message);
	}
	
	/**
	 * 临时队列名由deploy-${spring.application.name}-${spring.cloud.client.ip-address}-${server.port}构成
	 * 每个服务（包括同个服务的集群部署）都要有单独的队列来监听fanout exchange的消息（用于监听其他服务下线或上线事件，重新拉取注册信息）
	 * 
	 * @param msg
	 */
	@RabbitListener(bindings = @QueueBinding(value = @Queue(value = "deploy-${spring.application.name}-${spring.cloud.client.ip-address}-${server.port}", durable = "false", autoDelete = "true", exclusive = "true"), exchange = @Exchange(value = EXCHANGE, type = ExchangeTypes.FANOUT, durable = "false", autoDelete = "true")))
	public void handler(String msg) {
		boolean result = refreshHandler.refreshRegistry();
		log.info("#### refresh msg:{},result:{}", msg, result);
	}
}
