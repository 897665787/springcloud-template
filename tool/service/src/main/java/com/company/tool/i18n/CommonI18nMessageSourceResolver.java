package com.company.tool.i18n;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.company.framework.i18n.MessageSourceResolver;
import com.company.tool.entity.CommonI18n;
import com.company.tool.service.CommonI18nService;

import java.util.Locale;

/**
 * 基于 common_i18n 表的消息解析器
 *
 * <p>
 * 与 {@link CommonI18nDataProvider} 的区别：后者面向「业务实体字段」级翻译（按表/字段/实体ID批量扫描）， 本解析器面向「消息编码」级翻译（如
 * test.hello、test.hello.name），供{@code MysqlMessageSource} 使用。
 *
 * @author JQ棣
 */
@Component
public class CommonI18nMessageSourceResolver implements MessageSourceResolver {

    /** 全局/系统消息在 common_i18n 中的 business_id 占位值（不绑定具体业务实体行） */
    private static final int GLOBAL_BUSINESS_ID = 0;

    @Autowired
    private CommonI18nService commonI18nService;

    @Override
    public String resolve(String code, Locale locale) {
        CommonI18n commonI18n =
            commonI18nService.selectByBusinessTypeBusinessIdLocale(code, GLOBAL_BUSINESS_ID, locale.toLanguageTag());
        if (commonI18n == null) {
            return null;
        }
        return commonI18n.getI18nText();
    }
}
