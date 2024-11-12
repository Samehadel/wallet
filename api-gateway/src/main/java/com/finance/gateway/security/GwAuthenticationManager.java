package com.finance.gateway.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.Payload;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.finance.common.dto.AccessUri;
import com.finance.common.dto.AuthenticationDTO;
import com.finance.common.constants.CommonHeaders;
import com.finance.common.constants.UserStatus;
import com.finance.common.util.CollectionUtil;
import com.finance.gateway.AuthenticationService;
import com.finance.gateway.dto.GwAuthorizationContext;
import com.finance.gateway.dto.PathPatternAccessUri;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpHeaders;
import org.springframework.http.server.PathContainer;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.ReactiveAuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.authorization.AuthorizationContext;
import org.springframework.stereotype.Component;
import org.springframework.web.util.pattern.PathPattern;
import org.springframework.web.util.pattern.PathPatternParser;

import lombok.RequiredArgsConstructor;

import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class GwAuthenticationManager implements ReactiveAuthorizationManager<AuthorizationContext> {
    private final AuthenticationService authenticationService;
    private final ObjectMapper objectMapper;

    @Override
    public Mono<AuthorizationDecision> check(final Mono<Authentication> authentication, final AuthorizationContext object) {
        return authentication
            .flatMap(this::getAuthenticationDTOMono)
            .filter(authenticationDTO -> authenticationDTO.getWalletUser().getStatus() == UserStatus.ACTIVE)
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

    private Mono<AuthenticationDTO> getAuthenticationDTOMono(final Authentication a) {
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
