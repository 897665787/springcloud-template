package com.company.web.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;

//@Configuration
public class I18nWebConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 如果需要使用请求参数?lang=zh-CN来切换语言，则需要添加LocaleChangeInterceptor，但与AcceptHeaderLocaleResolver冲突
        LocaleChangeInterceptor interceptor = new LocaleChangeInterceptor();
        interceptor.setParamName("lang"); // 设置请求参数名为 "lang"，默认为 "locale"
        registry.addInterceptor(interceptor);
    }
}
