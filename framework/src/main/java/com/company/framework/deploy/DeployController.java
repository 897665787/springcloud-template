package com.company.framework.deploy;

import com.company.common.api.Result;
import com.company.framework.context.SpringContextUtil;
import com.company.framework.messagedriven.MessageSender;
import com.company.framework.messagedriven.constants.FanoutConstants;
import com.google.common.collect.Maps;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.springframework.amqp.rabbit.listener.RabbitListenerEndpointRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.serviceregistry.Registration;
import org.springframework.cloud.client.serviceregistry.ServiceRegistry;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.Optional;

/**
 * 部署相关接口（用于优雅发版）
 * 
 * @author JQ棣
 *
 */
@RestController
public class DeployController {

	@Autowired(required = false)
	private RabbitListenerEndpointRegistry rabbitListenerEndpointRegistry;

	@Autowired
	private MessageSender messageSender;
	
	/**
	 * 服务下线
	 * 
	 * @return
	 */
	@RequestMapping(value = "/offline", method = RequestMethod.GET)
	public Result<?> offline() {
		try {
			ServiceRegistry<Registration> serviceRegistry = SpringContextUtil.getBean(ServiceRegistry.class);
			Registration registration = SpringContextUtil.getBean(Registration.class);
			serviceRegistry.deregister(registration);

			// 通知其他服务刷新服务列表，即时中断请求流量
			Map<String, Object> params = Maps.newHashMap();
			params.put("application", SpringContextUtil.getProperty("spring.application.name"));
			params.put("type", "offline");
			messageSender.sendFanoutMessage(params, FanoutConstants.DEPLOY.EXCHANGE);
			
			// 下线MQ消费者
			Optional.ofNullable(rabbitListenerEndpointRegistry).ifPresent(RabbitListenerEndpointRegistry::stop);
			return Result.success();
		} catch (Exception e) {
			return Result.fail(ExceptionUtils.getMessage(e));
		}
	}

}
