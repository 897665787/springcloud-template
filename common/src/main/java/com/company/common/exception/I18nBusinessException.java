package com.company.common.exception;

import lombok.Getter;

import java.text.MessageFormat;

/**
 * 业务异常（建议交由全局异常处理器 GlobalExceptionHandler 处理抛出该异常，否则直接抛出RuntimeException或者对应含义的异常就好）
 *
 * @author JQ棣
 */
public class I18nBusinessException extends BusinessException {
    private static final long serialVersionUID = 1L;

    @Getter
    private Object[] args;

    public I18nBusinessException(Integer code, String message, Object... args) {
        super(code, message);
        this.args = args;
    }

    public I18nBusinessException(String message, Object... args) {
        super(message);
        this.args = args;
    }

    public I18nBusinessException(String message, Throwable e, Object... args) {
        super(message, e);
        this.args = args;
    }

    @Override
    public String getMessage() {
        return getLocalizedMessage();
    }

    @Override
    public String getLocalizedMessage() {
        if (args != null && args.length > 0) {
            MessageFormat messageFormat = new MessageFormat(super.getMessage());
            return messageFormat.format(args);
        }
        return super.getMessage();
    }
}
