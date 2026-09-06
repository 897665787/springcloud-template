package com.company.framework.i18n;

import org.springframework.beans.factory.ObjectProvider;
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
    public MessageSource mysqlMessageSource(MessageSource messageSource, ObjectProvider<MessageResolver> resolverProvider) {
        MysqlMessageSource mysqlMessageSource = new MysqlMessageSource();
        mysqlMessageSource.setParentMessageSource(messageSource);
        return mysqlMessageSource;
    }
}
