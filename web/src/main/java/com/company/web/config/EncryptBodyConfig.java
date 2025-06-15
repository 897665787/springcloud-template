package com.company.web.config;

import com.company.framework.encryptbody.annotation.EnableEncryptBody;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableEncryptBody
@ConditionalOnProperty(prefix = "encrypt.body", name = "enabled", havingValue = "true", matchIfMissing = false)
public class EncryptBodyConfig {
}
