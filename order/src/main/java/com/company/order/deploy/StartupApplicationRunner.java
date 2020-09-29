package com.company.order.deploy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * 服务启动成功回调（用于优雅发版）
 * 
 * @author jqd
 */
@Component
public class StartupApplicationRunner implements ApplicationRunner {

	@Autowired
	private RabbitMqHandler rabbitMqHandler;

	@Override
	public void run(ApplicationArguments args) throws Exception {
		rabbitMqHandler.notify2Refresh("startup");
	}

}