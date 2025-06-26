package com.company.framework.deploy;

import com.company.common.api.Result;
import com.company.framework.context.SpringContextUtil;
import com.company.framework.messagedriven.MessageSender;
import com.company.framework.messagedriven.constants.FanoutConstants;
import com.google.common.collect.Maps;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.rocketmq.spring.support.DefaultRocketMQListenerContainer;
import org.springframework.amqp.rabbit.listener.RabbitListenerEndpointRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.serviceregistry.Registration;
import org.springframework.cloud.client.serviceregistry.ServiceRegistry;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
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

	@Autowired
	private MessageSender messageSender;

	@Autowired(required = false)
	private ServiceRegistry serviceRegistry; // 注册中心：eureka | nacos
	@Autowired(required = false)
	private Registration registration; // 注册中心当前服务：eureka | nacos

	@Autowired(required = false)
	private RabbitListenerEndpointRegistry rabbitListenerEndpointRegistry;// RabbitMQ
	@Autowired(required = false)
	private List<DefaultRocketMQListenerContainer> defaultRocketMQListenerContainerList; // RocketMQ

	/**
	 * 服务下线
	 * 
	 * @return
	 */
	@RequestMapping(value = "/offline", method = RequestMethod.GET)
	public Result<?> offline() {
		try {
			// 下线注册中心
			Optional.ofNullable(serviceRegistry).ifPresent(v -> v.deregister(registration));
			// 通知其他服务刷新服务列表，即时中断请求流量
			Map<String, Object> params = Maps.newHashMap();
			params.put("application", SpringContextUtil.getProperty("spring.application.name"));
			params.put("type", "offline");
			messageSender.sendFanoutMessage(params, FanoutConstants.DEPLOY.EXCHANGE);

			// 下线RabbitMQ消费者
			Optional.ofNullable(rabbitListenerEndpointRegistry).ifPresent(RabbitListenerEndpointRegistry::stop);
			// 下线RocketMQ消费者
			if (CollectionUtils.isNotEmpty(defaultRocketMQListenerContainerList)) {
				defaultRocketMQListenerContainerList.forEach(DefaultRocketMQListenerContainer::destroy);
			}
			return Result.success();
		} catch (Exception e) {
			return Result.fail(ExceptionUtils.getMessage(e));
		}
	}

}
