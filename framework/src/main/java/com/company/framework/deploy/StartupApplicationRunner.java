package com.company.framework.deploy;

import java.util.Map;

import com.company.framework.trace.TraceManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import com.company.framework.messagedriven.MessageSender;
import com.company.framework.messagedriven.constants.FanoutConstants;
import com.company.framework.context.SpringContextUtil;
import com.google.common.collect.Maps;

import lombok.extern.slf4j.Slf4j;

/**
 * 服务启动成功回调（用于服务启动后马上通知其他服务刷新服务列表，即时获得请求流量）
 *
 * @author JQ棣
 */
@Slf4j
@Component
public class StartupApplicationRunner implements ApplicationRunner {

	@Autowired
	private MessageSender messageSender;

	@Autowired
	private TraceManager traceManager;

	@Override
	public void run(ApplicationArguments args) throws Exception {
		traceManager.put();
		String application = SpringContextUtil.getProperty("spring.application.name");
		log.info("{} startup", application);

		Map<String, Object> params = Maps.newHashMap();
		params.put("application", application);
		params.put("type", "startup");
		messageSender.sendFanoutMessage(params, FanoutConstants.DEPLOY.EXCHANGE);
		traceManager.remove();
	}

}
