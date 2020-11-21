package com.company.framework.threadpool.tomcat;

import java.util.concurrent.TimeUnit;

import org.apache.catalina.connector.Connector;
import org.apache.coyote.http11.Http11NioProtocol;
import org.apache.tomcat.util.threads.TaskQueue;
import org.apache.tomcat.util.threads.TaskThreadFactory;
import org.apache.tomcat.util.threads.ThreadPoolExecutor;
import org.springframework.boot.web.embedded.tomcat.TomcatConnectorCustomizer;

/**
 * 修改tomcat内置线程池，使用自定义的线程池
 */
public class CustomTomcatConnectorCustomizer implements TomcatConnectorCustomizer {

	@Override
	public void customize(Connector connector) {
		Http11NioProtocol protocol = (Http11NioProtocol) connector.getProtocolHandler();
		TaskQueue taskqueue = new TaskQueue();
		TaskThreadFactory tf = new TaskThreadFactory(protocol.getName().replace("\"", "") + "-exec-", true, protocol.getThreadPriority());
		ThreadPoolExecutor executor = new TomcatThreadPoolExecutor(protocol.getMinSpareThreads(), protocol.getMaxThreads(), 60,
				TimeUnit.SECONDS, taskqueue, tf);
		taskqueue.setParent(executor);

		protocol.setExecutor(executor);
	}
}