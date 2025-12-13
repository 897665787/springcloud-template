package com.company.encryptbody;

import cn.licoy.encryptbody.config.EncryptBodyConfig;
import com.company.encryptbody.advice.EncryptResponseBodyAdvice;
import com.company.encryptbody.annotation.EnableEncryptBody;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;

//@Configuration 使用org.springframework.boot.autoconfigure.AutoConfiguration.imports装配bean
@EnableEncryptBody
@ConditionalOnProperty(prefix = "encrypt.body", name = "enabled", havingValue = "true", matchIfMissing = false)
public class EncryptBodyAutoConfiguration {

    @Bean
    public EncryptResponseBodyAdvice encryptResponseBodyAdvice(ObjectMapper objectMapper, EncryptBodyConfig config) {
        EncryptResponseBodyAdvice encryptResponseBodyAdvice = new EncryptResponseBodyAdvice(objectMapper, config);
        return encryptResponseBodyAdvice;
	}
}
