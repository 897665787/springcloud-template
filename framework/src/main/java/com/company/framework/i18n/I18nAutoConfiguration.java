package com.company.framework.i18n;

import com.company.framework.cache.ICache;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration(proxyBeanMethods = false)
public class I18nAutoConfiguration {

    /**
     * 替换默认的MessageSource，将默认MessageSource设置为本MessageSource的ParentMessageSource
     */
    @Primary
    @Bean
    @ConditionalOnBean(MessageSourceResolver.class)
    public MessageSource mysqlMessageSource(MessageSource messageSource, MessageSourceResolver messageSourceResolver,
        ICache cache) {
        MysqlMessageSource mysqlMessageSource = new MysqlMessageSource(messageSourceResolver, cache);
        mysqlMessageSource.setParentMessageSource(messageSource);
        return mysqlMessageSource;
    }
}
