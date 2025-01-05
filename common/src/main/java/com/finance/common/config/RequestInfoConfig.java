package com.finance.common.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.finance.common.service.request.RequestUserInfoService;
import com.finance.common.service.request.RequestUserInfoServiceImpl;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.web.context.annotation.RequestScope;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class RequestInfoConfig {

    @ConditionalOnMissingBean(RequestUserInfoService.class)
    @RequestScope(proxyMode = ScopedProxyMode.INTERFACES)
    @Bean
    public RequestUserInfoService requestInfoService(final HttpServletRequest request, final ObjectMapper objectMapper) {
        return new RequestUserInfoServiceImpl(request, objectMapper);
    }
}
