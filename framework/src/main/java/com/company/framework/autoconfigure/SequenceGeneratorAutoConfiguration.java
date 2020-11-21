package com.company.framework.autoconfigure;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.company.framework.sequence.SequenceGenerator;
import com.company.framework.sequence.snowflake.HutoolSnowflake;

@Configuration
@ConditionalOnProperty(prefix = "template", name = "sequence.datacenterId")
public class SequenceGeneratorAutoConfiguration {

	@Bean
	public SequenceGenerator sequenceGenerator(@Value("${server.port}") Integer port,
			@Value("${template.sequence.datacenterId}") Long datacenterId) {
		SequenceGenerator sequenceGenerator = new HutoolSnowflake(port, datacenterId);
		return sequenceGenerator;
	}
}
