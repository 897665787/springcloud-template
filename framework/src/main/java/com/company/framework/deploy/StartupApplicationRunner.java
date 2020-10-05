package com.company.framework.deploy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * 服务启动成功回调（用于服务启动后马上通知其他服务刷新服务列表，即时获得请求流量）
 * 
 * @author jqd
 */
@Component
public class StartupApplicationRunner implements ApplicationRunner {

	@Autowired
	private RefreshHandler refreshHandler;

	@Override
	public void run(ApplicationArguments args) throws Exception {
		refreshHandler.notify2Refresh("startup");
	}

}