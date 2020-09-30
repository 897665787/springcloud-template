package com.company.user.deploy;

import java.lang.reflect.Method;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.company.user.util.SpringContextUtil;
import com.netflix.discovery.DiscoveryClient;

import lombok.extern.slf4j.Slf4j;

/**
 * 部署相关接口
 * 
 * @author Candi
 *
 */
@Slf4j
@Component
public class RefreshHandler {
	
	@Autowired(required = false)
	private MQAutoRefresh mqAutoRefresh;
    
	/**
	 * 通知其他服务刷新注册服务列表
	 */
	public void notify2Refresh(String msg) {
		if (mqAutoRefresh == null) {
			log.info("mqAutoRefresh is null");
			return;
		}
		mqAutoRefresh.send(msg);
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
