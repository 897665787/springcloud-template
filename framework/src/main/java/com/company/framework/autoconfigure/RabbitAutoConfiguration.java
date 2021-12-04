package com.company.framework.autoconfigure;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.core.RabbitTemplate.ConfirmCallback;
import org.springframework.amqp.rabbit.core.RabbitTemplate.ReturnCallback;
import org.springframework.amqp.rabbit.support.CorrelationData;
import org.springframework.amqp.support.converter.ContentTypeDelegatingMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.amqp.support.converter.SimpleMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.amqp.RabbitProperties;
import org.springframework.boot.autoconfigure.condition.AllNestedConditions;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import com.company.common.util.HostUtil;
import com.company.common.util.JsonUtil;
import com.company.framework.autoconfigure.RabbitAutoConfiguration.RabbitCondition;
import com.company.framework.context.SpringContextUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@Conditional(RabbitCondition.class)
public class RabbitAutoConfiguration {

	@Autowired
	private RabbitProperties rabbitProperties;

	@Bean
	public MessageConverter messageConverter() {
		return new ContentTypeDelegatingMessageConverter(new SimpleMessageConverter());
	}

	@Primary // 先注册的connectionFactory才可以创建队列
	@Bean
	public ConnectionFactory connectionFactory() {
		CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
		connectionFactory.setAddresses(rabbitProperties.getAddresses());
		connectionFactory.setUsername(rabbitProperties.getUsername());
		connectionFactory.setPassword(rabbitProperties.getPassword());
		connectionFactory.setVirtualHost(rabbitProperties.getVirtualHost());
		return connectionFactory;
	}

	@Bean
	public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(
			CachingConnectionFactory connectionFactory, MessageConverter messageConverter) {
		connectionFactory.setPublisherConfirms(true);
		SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
		factory.setAcknowledgeMode(AcknowledgeMode.MANUAL);
		factory.setConnectionFactory(connectionFactory);
		factory.setMessageConverter(messageConverter);
		if (SpringContextUtil.isTestProfile()) {
			factory.setConsumerTagStrategy(a -> HostUtil.identity() + "-" + RandomStringUtils.randomAlphanumeric(22));
		}
		return factory;
	}

	@Bean
	public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory, MessageConverter messageConverter) {
		RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
		rabbitTemplate.setMessageConverter(messageConverter);
		rabbitTemplate.setMandatory(true);// Mandatory为true时,消息通过交换器无法匹配到队列会返回给生产者,为false时,匹配不到会直接被丢弃

		rabbitTemplate.setConfirmCallback(new ConfirmCallback() {
			@Override
			public void confirm(CorrelationData correlationData, boolean ack, String cause) {
				log.info("correlationData:{},ack:{},cause:{}", JsonUtil.toJsonString(correlationData), ack, cause);
			}
		});
		rabbitTemplate.setReturnCallback(new ReturnCallback() {
			@Override
			public void returnedMessage(Message message, int replyCode, String replyText, String exchange,
					String routingKey) {
				// 当消息通过交换器无法匹配到队列会返回给生产者，就会打印这个日志
				log.info("message:{},replyCode:{},replyText:{},exchange:{},routingKey:{}",
						JsonUtil.toJsonString(message), replyCode, replyText, exchange, routingKey);
			}
		});

		return rabbitTemplate;
	}

	public static class RabbitCondition extends AllNestedConditions {

		RabbitCondition() {
			super(ConfigurationPhase.PARSE_CONFIGURATION);
		}

		@ConditionalOnProperty(prefix = "template.enable", name = "rabbitmq", havingValue = "true")
		static class EnableProperty {
		}

		@ConditionalOnProperty(prefix = "spring.rabbitmq", name = "addresses")
		static class HostProperty {
		}

	}
}
