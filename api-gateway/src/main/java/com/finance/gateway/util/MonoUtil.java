package com.finance.gateway.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.finance.common.exception.ErrorDetails;
import com.finance.common.exception.SharedApplicationError;
import com.finance.common.model.ApiResponse;
import com.finance.common.model.StatusEnum;
import com.finance.common.util.ApiResponseBuilder;
import com.finance.common.util.ObjectMapperFactory;

import java.nio.charset.StandardCharsets;

import org.springframework.http.server.reactive.ServerHttpResponse;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import reactor.core.publisher.Mono;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class MonoUtil {

    public static Mono<Void> writeVoidMono(final ErrorDetails error, final ServerHttpResponse response) {
        ApiResponse<Void> apiResponse = ApiResponseBuilder.buildApiResponse(StatusEnum.FAILED, error, null);

        String json;
        try {
            json = ObjectMapperFactory.getSnakeCaseInstance().writeValueAsString(apiResponse);
        } catch (JsonProcessingException e) {
            return response.setComplete();
        }

        var dataBuffer = response.bufferFactory().wrap(json.getBytes(StandardCharsets.UTF_8));
        return response.writeWith(Mono.just(dataBuffer));
    }
}
