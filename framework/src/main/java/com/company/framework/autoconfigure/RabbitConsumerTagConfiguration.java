package com.company.framework.autoconfigure;

import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import com.company.common.util.HostUtil;

/**
 * 用于标记消费者，方便找出队列的消费者
 */
@Configuration
public class RabbitConsumerTagConfiguration implements InitializingBean {

	@Autowired(required = false)
	private List<SimpleRabbitListenerContainerFactory> factorys;

	@Override
	public void afterPropertiesSet() throws Exception {
		Optional.ofNullable(factorys).ifPresent(list -> list.stream().forEach(factory -> factory.setConsumerTagStrategy(
				a -> HostUtil.identity() + "-" + HostUtil.ip() + "-" + RandomStringUtils.randomAlphanumeric(22))));
	}
}
