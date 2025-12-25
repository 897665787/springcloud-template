package com.company.framework.globalresponse;

import com.company.common.api.ResultCode;
import com.company.common.exception.ResultException;

/**
 * 业务异常（建议交由全局异常处理器 GlobalExceptionHandler 处理抛出该异常，否则直接抛出RuntimeException或者对应含义的异常就好）
 *
 * @author JQ棣
 */
public class BusinessException extends ResultException {
    private static final long serialVersionUID = 1L;

    protected BusinessException(String code, String message) {
        super(code, message);
    }

    protected BusinessException(String message) {
        this(ResultCode.FAIL.getCode(), message);
    }
}
