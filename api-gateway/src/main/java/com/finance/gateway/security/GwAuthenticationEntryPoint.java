package com.finance.gateway.security;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.finance.common.exception.ExceptionService;
import com.finance.common.model.ApiResponse;
import com.finance.common.model.StatusEnum;
import com.finance.common.exception.SharedApplicationError;

import java.nio.charset.StandardCharsets;
import java.time.Instant;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.server.ServerAuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import lombok.RequiredArgsConstructor;

import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class GwAuthenticationEntryPoint implements ServerAuthenticationEntryPoint {
    private final ExceptionService exceptionService;
    private final ObjectMapper objectMapper;

    @Override
    public Mono<Void> commence(final ServerWebExchange exchange, final AuthenticationException ex) {
        try {
            final var response = exchange.getResponse();
            final var errorResponseJson = buildErrorResponseJson(response);
            final var dataBuffer = response.bufferFactory().wrap(errorResponseJson.getBytes(StandardCharsets.UTF_8));

            return response.writeWith(Mono.just(dataBuffer));
        } catch (Exception e) {
            return exchange.getResponse().setComplete();
        }
    }

    private String buildErrorResponseJson(final ServerHttpResponse response) throws JsonProcessingException {
        response.setStatusCode(HttpStatus.FORBIDDEN);
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);

        final var errorMessage = exceptionService.buildErrorDetails(SharedApplicationError.FORBIDDEN_ERROR, null).errorMessage();
        var body = ApiResponse.builder()
            .status(StatusEnum.FAILED)
            .date(Instant.now())
            .errorCode(SharedApplicationError.FORBIDDEN_ERROR.getErrorCode())
            .errorMessage(errorMessage)
            .build();

        return objectMapper.writeValueAsString(body);
    }
}
