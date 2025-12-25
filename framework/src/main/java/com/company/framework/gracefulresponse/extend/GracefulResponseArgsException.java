package com.company.framework.gracefulresponse.extend;

import com.feiniaojin.gracefulresponse.GracefulResponseException;

/**
 * 带参数的异常响应
 *
 * @author JQ棣
 */
public class GracefulResponseArgsException extends GracefulResponseException {

    /**
     * 参数
     */
    private Object[] args;

    public GracefulResponseArgsException() {
    }

    public GracefulResponseArgsException(Object[] args) {
        this.args = args;
    }

    public GracefulResponseArgsException(String msg, Object[] args) {
        super(msg);
        this.args = args;
    }

    public GracefulResponseArgsException(String code, String msg, Object[] args) {
        super(code, msg);
        this.args = args;
    }

    public GracefulResponseArgsException(String msg, Throwable cause, Object[] args) {
        super(msg, cause);
        this.args = args;
    }

    public GracefulResponseArgsException(String code, String msg, Throwable cause, Object[] args) {
        super(code, msg, cause);
        this.args = args;
    }

    public Object[] getArgs() {
        return this.args;
    }
}
