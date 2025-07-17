package com.company.framework.globalresponse;

import com.company.framework.context.SpringContextUtil;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

/**
 * 国际化参数业务异常（建议交由全局异常处理器 GlobalExceptionHandler 处理抛出该异常，否则直接抛出RuntimeException或者对应含义的异常就好）
 *
 * @author JQ棣
 */
class I18nBusinessException extends BusinessException {

    private static final long serialVersionUID = 1L;

    @Getter
    private Object[] args;

    protected I18nBusinessException(Integer code, String message, Object... args) {
        super(code, message);
        this.args = args;
    }

    protected I18nBusinessException(String message, Object... args) {
        super(message);
        this.args = args;
    }

    @Override
    public String getMessage() {
        String message = super.getMessage();
        if (StringUtils.isBlank(message)) {
            return message;
        }
        MessageSource messageSource = SpringContextUtil.getBean(MessageSource.class);
        if (messageSource == null) {
            return message;
        }
        return messageSource.getMessage(message, args, message, LocaleContextHolder.getLocale());
    }
}
