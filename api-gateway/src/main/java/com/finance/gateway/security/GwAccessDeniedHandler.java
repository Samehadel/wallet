package com.finance.gateway.security;

import com.finance.common.exception.ErrorDetails;
import com.finance.common.exception.ExceptionService;
import com.finance.gateway.util.MonoUtil;
import com.finance.common.exception.SharedApplicationError;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.server.authorization.ServerAccessDeniedHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import lombok.RequiredArgsConstructor;

import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class GwAccessDeniedHandler implements ServerAccessDeniedHandler {
    private final ExceptionService exceptionService;

    @Override
    public Mono<Void> handle(final ServerWebExchange exchange, final AccessDeniedException denied) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);

        ErrorDetails errorDetails = exceptionService.buildErrorDetails(SharedApplicationError.UNAUTHORIZED_ERROR, null);
        return MonoUtil.writeVoidMono(errorDetails, response);
    }
}
