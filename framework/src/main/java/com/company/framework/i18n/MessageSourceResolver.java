package com.company.framework.i18n;

import java.util.Locale;

/**
 * 消息文案解析器
 *
 * <p>
 * 框架层不直接依赖数据访问层，由下游模块提供具体实现。
 * <p>
 * 未提供实现时，{@code MysqlMessageSource} 会平滑降级到 Spring {@code MessageSource}（messages*.properties）。
 *
 * @author JQ棣
 */
public interface MessageSourceResolver {

    /**
     * 根据消息编码与 locale 查询翻译文案。
     *
     * @param code 消息编码
     * @param locale 地区编码，如 zh-CN、en-US
     * @return 命中的国际化文案；未命中返回 null
     */
    String resolve(String code, Locale locale);
}
