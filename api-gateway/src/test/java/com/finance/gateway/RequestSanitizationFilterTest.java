package com.finance.gateway;

import static org.assertj.core.api.Assertions.assertThat;

import com.finance.gateway.filter.RequestSanitizationFilter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.mock.http.server.reactive.MockServerHttpRequest;
import org.springframework.mock.web.server.MockServerWebExchange;
import org.springframework.web.server.ServerWebExchange;

import reactor.core.publisher.Mono;

class RequestSanitizationFilterTest {

    private RequestSanitizationFilter filter;
    private RequestSanitizationFilter.Config config;

    @BeforeEach
    void setUp() {
        filter = new RequestSanitizationFilter();
        config = new RequestSanitizationFilter.Config();
    }

    @Test
    void testSanitizeQueryParams() {
        MockServerHttpRequest request = MockServerHttpRequest
            .get("/test")
            .queryParam("name", "<script>alert('xss')</script>")
            .build();

        MockServerWebExchange exchange = MockServerWebExchange.from(request);
        GatewayFilter gatewayFilter = filter.apply(config);

        // Act
        gatewayFilter.filter(exchange, (ServerWebExchange chain) -> Mono.empty()).block();

        // Assert
        String sanitizedParam = exchange.getRequest().getQueryParams().getFirst("name");
        assertThat(sanitizedParam)
            .isNotNull()
            .doesNotContain("<script>");
    }
}
