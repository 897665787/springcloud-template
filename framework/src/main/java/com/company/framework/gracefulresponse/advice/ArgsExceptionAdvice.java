package com.company.framework.gracefulresponse.advice;

import com.company.framework.gracefulresponse.GracefulResponseArgsException;
import com.company.framework.gracefulresponse.context.GracefulResponseExceptionArgsContext;
import com.company.framework.message.IMessage;
import com.feiniaojin.gracefulresponse.GracefulResponseProperties;
import com.feiniaojin.gracefulresponse.advice.AbstractControllerAdvice;
import com.feiniaojin.gracefulresponse.advice.lifecycle.exception.*;
import com.feiniaojin.gracefulresponse.api.ResponseFactory;
import com.feiniaojin.gracefulresponse.api.ResponseStatusFactory;
import com.feiniaojin.gracefulresponse.data.Response;
import com.feiniaojin.gracefulresponse.data.ResponseStatus;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.annotation.Order;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 处理GracefulResponseArgsException的异常
 *
 * @author JQ棣
 */
@Order(195)
@ControllerAdvice
public class ArgsExceptionAdvice extends AbstractControllerAdvice
    implements ControllerAdvicePredicate, ControllerAdviceProcessor, ControllerAdviceHttpProcessor {

    private ResponseFactory responseFactory;
    private GracefulResponseProperties properties;
    private ResponseStatusFactory responseStatusFactory;

    public ArgsExceptionAdvice(BeforeControllerAdviceProcess beforeControllerAdviceProcess, @Lazy RejectStrategy rejectStrategy,
        ResponseFactory responseFactory, GracefulResponseProperties properties, ResponseStatusFactory responseStatusFactory) {

        this.responseFactory = responseFactory;
        this.properties = properties;
        this.responseStatusFactory = responseStatusFactory;

        setRejectStrategy(rejectStrategy);
        setControllerAdviceProcessor(this);
        setBeforeControllerAdviceProcess(beforeControllerAdviceProcess);
        setControllerAdviceHttpProcessor(this);
    }

    @Override
    public Response process(HttpServletRequest request, HttpServletResponse response, @Nullable Object handler, Exception exception) {
        if (exception instanceof GracefulResponseArgsException) {
            GracefulResponseArgsException dataException = (GracefulResponseArgsException) exception;
            ResponseStatus statusLine = fromGracefulResponseExceptionInstance(dataException);
            return responseFactory.newInstance(statusLine);
        }
        throw new UnsupportedOperationException();
    }

    private ResponseStatus fromGracefulResponseExceptionInstance(GracefulResponseArgsException exception) {
        String code = exception.getCode();
        if (code == null) {
            code = properties.getDefaultErrorCode();
        }
        // 记录异常中的args到上下文，后续会在GrI18nResponseBodyAdvice.process统一处理msg
        GracefulResponseExceptionArgsContext.setArgs(exception.getArgs());
        return responseStatusFactory.newInstance(code, exception.getMsg());
    }

    @Override
    @ExceptionHandler(value = GracefulResponseArgsException.class)
    public Object exceptionHandler(Exception exception) {
        return super.exceptionHandler(exception);
    }

    @Override
    public boolean shouldApplyTo(HttpServletRequest request, HttpServletResponse response, @Nullable Object handler, Exception exception) {
        return exception instanceof GracefulResponseArgsException;
    }
}


