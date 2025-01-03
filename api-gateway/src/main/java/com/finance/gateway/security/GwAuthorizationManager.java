package com.finance.gateway.security;

import com.finance.common.constants.CommonHeaders;
import com.finance.common.constants.RequestSource;
import com.finance.gateway.dto.GwAuthorizationContext;
import com.finance.gateway.service.AuthorizationService;

import java.util.Optional;

import org.springframework.http.HttpHeaders;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.ReactiveAuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.authorization.AuthorizationContext;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class GwAuthorizationManager implements ReactiveAuthorizationManager<AuthorizationContext> {
    private final AuthorizationService authorizationService;

    @Override
    public Mono<AuthorizationDecision> check(final Mono<Authentication> authentication, final AuthorizationContext context) {
        return authentication
            .map(this::getAuthenticationDTOMono)
            .flatMap(token -> processAuthenticationContext(token, context))
            .defaultIfEmpty(new AuthorizationDecision(false));
    }

    private String getAuthenticationDTOMono(final Authentication a) {
        return Optional.ofNullable(a)
            .map(auth -> auth.getCredentials().toString())
            .orElse(null);
    }

    private Mono<AuthorizationDecision> processAuthenticationContext(final String token, final AuthorizationContext context) {
        return authorizationService
            .getAuthenticationContext(token, context.getExchange().getRequest())
            .map(authContext -> {
                if (authContext.authorizationDecision().isGranted()) {
                    mutateRequest(context, authContext);
                }
                return authContext.authorizationDecision();
            });
    }

    private void mutateRequest(final AuthorizationContext context, final GwAuthorizationContext authContext) {
        context.getExchange()
            .mutate()
            .request(requestBuilder -> requestBuilder
                .header(CommonHeaders.X_USER, authContext.walletUser())
                .header(CommonHeaders.X_REQUEST_SOURCE, RequestSource.EXTERNAL.getValue())
                .headers(headers -> headers.remove(HttpHeaders.AUTHORIZATION)))
            .build();
    }
}
