package com.nisumpruebatecnica.musers.config;

import com.nisumpruebatecnica.musers.constants.BasicConstants;
import com.nisumpruebatecnica.musers.interceptor.RequestInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@RequiredArgsConstructor
@Component
public class RequestInterceptorConfig implements WebMvcConfigurer {

    private final RequestInterceptor requestInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(requestInterceptor).excludePathPatterns(BasicConstants.WHITELIST);
    }

}
