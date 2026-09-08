package com.company.framework.i18n;

import java.text.MessageFormat;
import java.util.Locale;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.context.support.AbstractMessageSource;

import com.company.framework.cache.ICache;

import lombok.extern.slf4j.Slf4j;

/**
 * 基于 MySQL 表存储的国际化消息实现
 *
 * @author JQ棣
 */
@Slf4j
public class MysqlMessageSource extends AbstractMessageSource {
    private static final String CACHE_KEY_PREFIX = "i18n";

    private final MessageSourceResolver messageSourceResolver;

    private final ICache cache;

    public MysqlMessageSource(MessageSourceResolver messageSourceResolver, ICache cache) {
        this.messageSourceResolver = messageSourceResolver;
        this.cache = cache;
    }

    @Override
    protected MessageFormat resolveCode(String code, Locale locale) {
        String key = String.format("%s:%s:%s", CACHE_KEY_PREFIX, code, locale.toLanguageTag());
//        String pattern = cache.get(key, () -> messageSourceResolver.resolve(code, locale));
        String pattern = messageSourceResolver.resolve(code, locale);
        if (StringUtils.isBlank(pattern)) {
            return null;
        }
        return new MessageFormat(pattern, locale);
    }
}
