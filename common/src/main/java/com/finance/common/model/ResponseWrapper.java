package com.finance.common.model;

import java.time.Instant;

import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import lombok.extern.slf4j.Slf4j;

@ControllerAdvice
@Order
@Slf4j
public class ResponseWrapper implements ResponseBodyAdvice<Object> {

    @Override
    public boolean supports(final MethodParameter methodParameter, final Class clazz) {
        var method = methodParameter.getMethod();
        return method == null || !method.getName().contains("getDocumentation");
    }

    @Override
    public Object beforeBodyWrite(final Object body,
        final MethodParameter methodParameter,
        final MediaType mediaType,
        final Class clazz,
        final ServerHttpRequest serverHttpRequest,
        final ServerHttpResponse serverHttpResponse) {

        if (body instanceof ApiResponse
            || !mediaType.includes(MediaType.APPLICATION_JSON)
            || !methodParameter.getDeclaringClass().getPackageName().startsWith("com.warba")) {
            return body;
        }

        var response = new ApiResponse<>();
        response.setStatus(StatusEnum.SUCCESS);
        response.setDate(Instant.now());
        response.setResponseBody(body);

        return response;
    }
}