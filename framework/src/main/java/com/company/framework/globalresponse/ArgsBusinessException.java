package com.company.framework.globalresponse;

import lombok.Getter;

/**
 * 参数业务异常（建议交由全局异常处理器 GlobalExceptionHandler 处理抛出该异常，否则直接抛出RuntimeException或者对应含义的异常就好）
 *
 * @author JQ棣
 */
class ArgsBusinessException extends BusinessException {

    private static final long serialVersionUID = 1L;

    @Getter
    private Object[] args;

    protected ArgsBusinessException(Integer code, String message, Object... args) {
        super(code, message);
        this.args = args;
    }

    protected ArgsBusinessException(String message, Object... args) {
        super(message);
        this.args = args;
    }
}
