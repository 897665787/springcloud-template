package com.company.framework.i18n;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
import org.springframework.context.support.AbstractMessageSource;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

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

    private Cache<String, Optional<String>> guavaCache = CacheBuilder.newBuilder()//
            .maximumSize(10000)//
            .expireAfterWrite(600, TimeUnit.SECONDS)//
            .removalListener(listener -> {
                log.info("key:{},value:{},cause:{}", listener.getKey(), listener.getValue(), listener.getCause());
            }).build();

    public MysqlMessageSource(MessageSourceResolver messageSourceResolver) {
        this.messageSourceResolver = messageSourceResolver;
    }

    @Override
    protected MessageFormat resolveCode(String code, Locale locale) {
        String key = String.format("%s:%s:%s", CACHE_KEY_PREFIX, code, locale.toLanguageTag());
        // String pattern = messageSourceResolver.resolve(code, locale);
        String pattern;
        try {
            Optional<String> optional = guavaCache.get(key, () -> {
                String resolve = messageSourceResolver.resolve(code, locale);
                if (StringUtils.isBlank(resolve)) {
                    return Optional.empty();
                }
                return Optional.of(resolve);
            });
            pattern = optional.orElse(null);
        } catch (ExecutionException e) {
            return null;
        }
        if (StringUtils.isBlank(pattern)) {
            return null;
        }
        return new MessageFormat(pattern, locale);
    }
}
