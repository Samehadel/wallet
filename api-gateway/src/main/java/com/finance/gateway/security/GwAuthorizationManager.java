package com.finance.gateway.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.Payload;
import com.finance.common.constants.CommonHeaders;
import com.finance.common.dto.AuthResultDTO;
import com.finance.gateway.dto.GwAuthorizationContext;
import com.finance.gateway.service.AuthenticationService;

import java.util.Optional;

import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
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
    private final AuthenticationService authenticationService;

    @Override
    public Mono<AuthorizationDecision> check(final Mono<Authentication> authentication, final AuthorizationContext object) {
        return authentication
            .flatMap(this::getAuthenticationDTOMono)
            .map(authenticationResponse -> {
                ServerHttpRequest request = object.getExchange().getRequest();
                GwAuthorizationContext authenticationContext =
                    authenticationService.getAuthenticationContext(authenticationResponse, request);

                if (authenticationContext.authorizationDecision().isGranted()) {
                    request.mutate()
                        .header(CommonHeaders.X_USER, authenticationContext.walletUser())
                        .headers(h -> h.remove(HttpHeaders.AUTHORIZATION))
                        .build();
                }

                return authenticationContext.authorizationDecision();
            })
            .defaultIfEmpty(new AuthorizationDecision(false));
    }

    private Mono<AuthResultDTO> getAuthenticationDTOMono(final Authentication a) {
        final var jwt = new JWT();
        final var username = Optional.ofNullable(a.getPrincipal())
            .map(Object::toString)
            .map(jwt::decodeJwt)
            .map(Payload::getClaims)
            .map(m -> m.get("username"))
            .map(Claim::asString);
        if (username.isEmpty()) {
            return Mono.empty();
        }
        return authenticationService.getAuthenticationCall(username.get());
    }
}
