package com.company.framework.gracefulshutdown;

import com.company.framework.trace.TraceManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 服务启动成功回调（用于服务启动后马上通知其他服务刷新服务列表，即时获得请求流量）
 *
 * @author JQ棣
 */
@Slf4j
@Component
public class StartupApplicationRunner implements ApplicationRunner {

	@Autowired
	private TraceManager traceManager;

	@Autowired(required = false)
	private List<InstanceStartup> instanceStartupList;

	@Override
	public void run(ApplicationArguments args) throws Exception {
		traceManager.put();
		if (instanceStartupList == null) {
			return;
		}
		instanceStartupList.forEach(InstanceStartup::startup);
		traceManager.remove();
	}

}
