package com.company.order.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.netflix.discovery.DiscoveryManager;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class ServiceController {

	@Autowired
    private DiscoveryClient client;

	@RequestMapping(value = "/instances", method = RequestMethod.GET)
	public List<ServiceInstance> index() {
		List<ServiceInstance> instances = client.getInstances("template-order");
		log.info("instances:{}", instances);
		return instances;
	}
    
    @RequestMapping(value = "/offline", method = RequestMethod.GET)
    public void offLine(){
    	DiscoveryManager.getInstance();
        DiscoveryManager.getInstance().shutdownComponent();
    } 
}
