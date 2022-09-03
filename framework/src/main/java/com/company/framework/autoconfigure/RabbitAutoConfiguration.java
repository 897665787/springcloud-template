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
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import com.company.common.util.HostUtil;
import com.company.common.util.JsonUtil;
import com.company.framework.amqp.rabbit.constants.FanoutConstants;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
//@Conditional(RabbitCondition.class)
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
		connectionFactory.setPublisherConfirms(true);// publiser-confirm模式可以确保生产者到交换器exchange消息有没有发送成功
		SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
		factory.setAcknowledgeMode(AcknowledgeMode.MANUAL);
		factory.setConnectionFactory(connectionFactory);
		factory.setMessageConverter(messageConverter);
		factory.setConsumerTagStrategy(
				a -> HostUtil.identity() + "-" + HostUtil.ip() + "-" + RandomStringUtils.randomAlphanumeric(22));
		return factory;
	}

	@Bean
	public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory, MessageConverter messageConverter) {
		RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
		rabbitTemplate.setMessageConverter(messageConverter);
		rabbitTemplate.setMandatory(true);// 使用publiser-confirm，publisher-returns属性

		rabbitTemplate.setConfirmCallback(new ConfirmCallback() {
			@Override
			public void confirm(CorrelationData correlationData, boolean ack, String cause) {
				// publiser-confirm模式可以确保生产者到交换器exchange消息有没有发送成功
				log.info("correlationData:{},ack:{},cause:{}", JsonUtil.toJsonString(correlationData), ack, cause);
			}
		});
		rabbitTemplate.setReturnCallback(new ReturnCallback() {
			@Override
			public void returnedMessage(Message message, int replyCode, String replyText, String exchange,
					String routingKey) {
				// 当消息通过交换器无法匹配到队列会返回给生产者，就会打印这个日志
				if (exchange != null && exchange.startsWith(FanoutConstants.PREFIX)) {
					// (并非是BUG)如果配置了发送回调ReturnCallback，插件延迟队列则会回调该方法，因为发送方确实没有投递到队列上，只是在交换器上暂存，等过期时间到了才会发往队列
					return;
				}
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
