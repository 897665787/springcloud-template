package com.company.framework.globalresponse.gracefulresponse;//package com.company.framework.globalresponse;

import org.springframework.context.annotation.Configuration;

import com.company.framework.globalresponse.UnauthorizedException;
import com.feiniaojin.gracefulresponse.AbstractExceptionAliasRegisterConfig;
import com.feiniaojin.gracefulresponse.ExceptionAliasRegister;

@Configuration
public class GracefulResponseConfig extends AbstractExceptionAliasRegisterConfig {

    @Override
    protected void registerAlias(ExceptionAliasRegister aliasRegister) {
        //注册异常别名
        aliasRegister.doRegisterExceptionAlias(UnauthorizedException.class);
    }
}