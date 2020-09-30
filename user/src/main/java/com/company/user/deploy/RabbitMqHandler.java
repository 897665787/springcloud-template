package com.company.user.deploy;

import java.lang.reflect.Method;

import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.company.user.util.SpringContextUtil;
import com.netflix.discovery.DiscoveryClient;

import lombok.extern.slf4j.Slf4j;

/**
 * 部署相关接口
 * 
 * @author jqd
 *
 */
@Slf4j
@Component
public class RabbitMqHandler {
	private static final String EXCHANGE = "deploy";
	
	@Value("${spring.application.name}")
	private String applicationName;
	
	@Autowired
    @Qualifier("rabbitTemplate")
    private RabbitTemplate rabbitTemplate;
	
	/**
	 * 临时队列名由deploy-${spring.application.name}-${spring.cloud.client.ip-address}-${server.port}构成
	 * 每个服务（包括同个服务的集群部署）都要有单独的队列来监听fanout exchange的消息
	 * （用于监听其他服务下线或上线事件，重新拉取注册信息）
	 * 
	 * @param msg
	 */
	@RabbitListener(bindings = @QueueBinding(value = @Queue(value = "deploy-${spring.application.name}-${spring.cloud.client.ip-address}-${server.port}", durable = "false", autoDelete = "true", exclusive = "true"), exchange = @Exchange(value = EXCHANGE, type = ExchangeTypes.FANOUT, durable = "false", autoDelete = "true")))
	public void handler(String msg) {
		boolean result = refreshRegistry();
		log.info("#### refresh msg:{},result:{}", msg, result);
	}
    
	/**
	 * 往MQ发送一条广播消息，通知其他服务刷新注册服务列表
	 */
	public void notify2Refresh(String msg) {
		String message = applicationName + "->" + msg;
		log.info("#### send,msg:{}", message);
		this.rabbitTemplate.convertAndSend(EXCHANGE, "", message);
	}
    
	/**
	 * 刷新注册列表
	 */
	public boolean refreshRegistry() {
		try {
			Method method = DiscoveryClient.class.getDeclaredMethod("refreshRegistry");
			method.setAccessible(true);
			method.invoke(SpringContextUtil.getBean(DiscoveryClient.class));
			log.info("refresh success!!!");
			return true;
		} catch (Exception e) {
			log.error("refresh fail!!!", e);
			return false;
		}
	}
}
