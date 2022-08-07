package com.company.job.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.company.order.api.feign.OrderFeign;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class UserService {
	
	@Autowired
	private OrderFeign orderFeign;
	
    @XxlJob("demoJobHandler")
    public ReturnT<String> demoJobHandler(String param){
        log.info("hello world11111");
        orderFeign.getById(1L);
        XxlJobHelper.log("hello world22222222");
        return ReturnT.SUCCESS;
    }
}
