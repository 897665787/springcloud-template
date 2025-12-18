package com.company.framework.gracefulresponse;

import com.feiniaojin.gracefulresponse.GracefulResponseException;

public class GracefulResponseExceptionContext {
    private static ThreadLocal<GracefulResponseException> exceptionThreadLocal = new ThreadLocal<>();

    public static GracefulResponseException getException() {
        return exceptionThreadLocal.get();
    }

    public static void setException(GracefulResponseException exception) {
        exceptionThreadLocal.set(exception);
    }

    public static void removeException() {
        exceptionThreadLocal.remove();
    }

}
