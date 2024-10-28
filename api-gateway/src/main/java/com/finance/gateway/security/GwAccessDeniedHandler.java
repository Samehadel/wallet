package com.finance.gateway.security;

import com.finance.gateway.util.MonoUtil;
import com.finance.common.exception.SharedApplicationError;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.server.authorization.ServerAccessDeniedHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import reactor.core.publisher.Mono;

@Component
public class GwAccessDeniedHandler implements ServerAccessDeniedHandler {

    @Override
    public Mono<Void> handle(final ServerWebExchange exchange, final AccessDeniedException denied) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);

        return MonoUtil.writeVoidMono(SharedApplicationError.UNAUTHORIZED_ERROR, response);
    }
}
