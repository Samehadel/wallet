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

import lombok.RequiredArgsConstructor;

import reactor.core.publisher.Mono;

@Configuration
@EnableWebFluxSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain configure(
        final ServerHttpSecurity http,
        final ReactiveAuthorizationManager<AuthorizationContext> authorizationManager,
        final ServerAuthenticationEntryPoint entryPoint,
        final ServerAccessDeniedHandler accessDeniedHandler) {

        return http
            .oauth2ResourceServer(resourceServerSpec ->
                resourceServerSpec
                    .authenticationManagerResolver(c -> Mono.just(Mono::just))
                    .authenticationEntryPoint(entryPoint)
                    .accessDeniedHandler(accessDeniedHandler)
            )
            .authorizeExchange(authorizeExchangeSpec ->
                authorizeExchangeSpec
                    .pathMatchers(
                        "/actuator/health",
                        "/actuator/health/**",
                        "/actuator/prometheus",
                        "/unauthenticated",
                        "/oauth2/**",
                        "/auth/login/**",
                        "/user/register/**").permitAll()
                    .anyExchange().access(authorizationManager)
            ).build();
    }
}
