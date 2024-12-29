package com.finance.common.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.finance.common.service.request.RequestInfoService;
import com.finance.common.service.request.RequestInfoServiceImpl;

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

    @ConditionalOnMissingBean(RequestInfoService.class)
    @RequestScope(proxyMode = ScopedProxyMode.INTERFACES)
    @Bean
    public RequestInfoService requestInfoService(final HttpServletRequest request, final ObjectMapper objectMapper) {
        return new RequestInfoServiceImpl(request, objectMapper);
    }
}
