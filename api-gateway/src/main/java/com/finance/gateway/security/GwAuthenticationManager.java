package com.finance.gateway.security;

import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.ReactiveAuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.authorization.AuthorizationContext;
import org.springframework.stereotype.Component;

import reactor.core.publisher.Mono;

@Component
public class GwAuthenticationManager implements ReactiveAuthorizationManager<AuthorizationContext> {

    @Override
    public Mono<AuthorizationDecision> check(final Mono<Authentication> authentication, final AuthorizationContext object) {
        return Mono.just(new AuthorizationDecision(false));
    }
}
