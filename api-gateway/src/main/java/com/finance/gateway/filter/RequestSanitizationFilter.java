package com.finance.gateway.filter;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.server.ServerWebExchange;

import lombok.Getter;
import lombok.Setter;

import reactor.core.publisher.Mono;

@Component
public class RequestSanitizationFilter extends AbstractGatewayFilterFactory<RequestSanitizationFilter.Config> {

    public RequestSanitizationFilter() {
        super(Config.class);
    }

    @Setter
    @Getter
    public static class Config {
        private boolean sanitizeHeaders = true;
        private boolean sanitizeQueryParams = true;
        private boolean sanitizeBody = true;

    }

    @Override
    public String name() {
        return "RequestSanitization";
    }

    @Override
    public GatewayFilter apply(final Config config) {
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();

            // Create a new mutable request
            ServerHttpRequest.Builder builder = request.mutate();

            // Sanitize query parameters
            if (config.isSanitizeQueryParams()) {
                sanitizeQueryParams(builder, request);
            }

            // Sanitize headers
            if (config.isSanitizeHeaders()) {
                sanitizeHeaders(builder, request);
            }

            // Sanitize body for specific content types
            if (config.isSanitizeBody() && isJsonRequest(request)) {
                return sanitizeBody(exchange, chain);
            }

            return chain.filter(exchange.mutate().request(builder.build()).build());
        };
    }

    private void sanitizeQueryParams(ServerHttpRequest.Builder builder, ServerHttpRequest request) {
        MultiValueMap<String, String> params = request.getQueryParams();
        MultiValueMap<String, String> sanitizedParams = new LinkedMultiValueMap<>();

        params.forEach((key, values) -> {
            List<String> sanitizedValues = new ArrayList<>();
            values.forEach(value -> sanitizedValues.add(sanitizeString(value)));
            sanitizedParams.add(sanitizeString(key), sanitizedValues.get(0));
        });

        // Create a new URI with sanitized query parameters
        String path = request.getPath().value();
        String newQuery = sanitizedParams.isEmpty() ? "" : "?" + formatQueryParams(sanitizedParams);
        builder.path(path).uri(URI.create(path + newQuery));
    }

    private String formatQueryParams(MultiValueMap<String, String> params) {
        StringBuilder result = new StringBuilder();
        params.forEach((key, values) -> values.forEach(value -> {
            if (!result.isEmpty()) {
                result.append("&");
            }

            result.append(key).append("=").append(value);
        }));

        return result.toString();
    }

    private void sanitizeHeaders(ServerHttpRequest.Builder builder, ServerHttpRequest request) {
        request.getHeaders().forEach((key, values) -> {
            if (!key.equalsIgnoreCase("content-length")) {
                List<String> sanitizedValues = new ArrayList<>();
                values.forEach(value -> sanitizedValues.add(sanitizeString(value)));
                builder.header(key, sanitizedValues.toArray(new String[0]));
            }
        });
    }

    private Mono<Void> sanitizeBody(final ServerWebExchange exchange, final GatewayFilterChain chain) {
        return DataBufferUtils.join(exchange.getRequest().getBody())
            .flatMap(dataBuffer -> {
                byte[] content = new byte[dataBuffer.readableByteCount()];
                dataBuffer.read(content);
                DataBufferUtils.release(dataBuffer);
                String bodyStr = new String(content, StandardCharsets.UTF_8);

                // Sanitize the body
                String sanitizedBody = sanitizeString(bodyStr);

                ServerHttpRequest mutatedRequest = exchange.getRequest().mutate()
                    .header("Content-Length", String.valueOf(sanitizedBody.length()))
                    .build();

                DataBuffer sanitizedBuffer = exchange.getResponse().bufferFactory()
                    .wrap(sanitizedBody.getBytes(StandardCharsets.UTF_8));

                return chain.filter(exchange.mutate()
                        .request(mutatedRequest)
                        .build())
                    .then(Mono.fromRunnable(() -> DataBufferUtils.release(sanitizedBuffer)));
            });
    }

    private boolean isJsonRequest(ServerHttpRequest request) {
        String contentType = request.getHeaders().getFirst("Content-Type");
        return contentType != null && contentType.contains(MediaType.APPLICATION_JSON_VALUE);
    }

    private String sanitizeString(String input) {
        if (input == null) {
            return null;
        }
        // Implement multiple layers of sanitization
        String sanitized = input;

        // XSS prevention
        sanitized = sanitized.replaceAll("<script>|</script>|javascript:|onerror=|onload=", "");

        // SQL injection prevention
        sanitized = sanitized.replaceAll("(?i)\\b(SELECT|INSERT|UPDATE|DELETE|DROP|UNION|ALTER)\\b", "");

        // Remove control characters
        sanitized = sanitized.replaceAll("[\\x00-\\x1F\\x7F]", "");

        return sanitized;
    }
}
