package com.company.order.deploy;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.company.common.api.Result;
import com.company.order.util.SpringContextUtil;
import com.netflix.discovery.DiscoveryClient;

/**
 * 部署相关接口（用于优雅发版）
 * 
 * @author jqd
 *
 */
@RestController
public class DeployController {
	
	@Autowired
	private RabbitMqHandler rabbitMqHandler;

	/**
	 * 服务下线
	 * 
	 * @return
	 */
	@RequestMapping(value = "/offline", method = RequestMethod.GET)
	public Result offLine() {
		try {
			DiscoveryClient client = SpringContextUtil.getBean(DiscoveryClient.class);
			client.shutdown();
			// 通知其他服务刷新服务列表，即时中断请求流量
			rabbitMqHandler.notify2Refresh("offline");
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
	public Result refreshRegistry() {
		return Result.success(rabbitMqHandler.refreshRegistry());
	}
}
