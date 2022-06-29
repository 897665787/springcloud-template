package com.company.framework.deploy;

import java.util.Optional;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.springframework.amqp.rabbit.listener.RabbitListenerEndpointRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.company.common.annotation.PublicUrl;
import com.company.common.api.Result;
import com.company.framework.context.SpringContextUtil;
import com.netflix.discovery.DiscoveryClient;

/**
 * 部署相关接口（用于优雅发版）
 * 
 * @author jqd
 *
 */
@PublicUrl
@RestController
public class DeployController {

	@Autowired
	private RefreshHandler refreshHandler;
	@Autowired(required = false)
	private RabbitListenerEndpointRegistry rabbitListenerEndpointRegistry;

	/**
	 * 服务下线
	 * 
	 * @return
	 */
	@RequestMapping(value = "/offline", method = RequestMethod.GET)
	public Result<?> offline() {
		try {
			DiscoveryClient client = SpringContextUtil.getBean(DiscoveryClient.class);
			client.shutdown();
			// 通知其他服务刷新服务列表，即时中断请求流量
			refreshHandler.notify2Refresh("offline");
			// 下线MQ消费者
			Optional.ofNullable(rabbitListenerEndpointRegistry).ifPresent(RabbitListenerEndpointRegistry::stop);
			return Result.success();
		} catch (Exception e) {
			return Result.fail(ExceptionUtils.getMessage(e));
		}
	}

	/**
	 * 刷新注册列表
	 * 
	 * @return
	 */
	@RequestMapping(value = "/refreshRegistry", method = RequestMethod.GET)
	public Result<?> refreshRegistry() {
		return Result.success(refreshHandler.refreshRegistry());
	}
}
