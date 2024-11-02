package com.finance.gateway.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authorization.ReactiveAuthorizationManager;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.ServerAuthenticationEntryPoint;
import org.springframework.security.web.server.authorization.AuthorizationContext;
import org.springframework.security.web.server.authorization.ServerAccessDeniedHandler;

import reactor.core.publisher.Mono;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain configure(final ServerHttpSecurity http,
        final ReactiveAuthorizationManager<AuthorizationContext> authenticationManager,
        final ServerAuthenticationEntryPoint authenticationEntryPoint,
        final ServerAccessDeniedHandler accessDeniedHandler) {

        return http
            .oauth2ResourceServer(oAuth2ResourceServerSpec -> oAuth2ResourceServerSpec
                .authenticationEntryPoint(authenticationEntryPoint)
                .accessDeniedHandler(accessDeniedHandler)
                .authenticationManagerResolver(c -> Mono.just(Mono::just))
            )
            .authorizeExchange(authorizeExchangeSpec -> authorizeExchangeSpec
                .pathMatchers("/actuator/health",
                    "/v3/api-docs",
                    "/swagger-ui.html",
                    "/swagger-ui/**",
                    "/actuator/health/**",
                    "/actuator/prometheus",
                    "/unauthenticated",
                    "/oauth2/**",
                    "/login/**",
                    "/register/**").permitAll()
                .anyExchange()
                .access(authenticationManager)
            ).build();
    }
}
