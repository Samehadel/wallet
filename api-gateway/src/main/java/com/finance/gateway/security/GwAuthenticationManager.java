package com.finance.gateway.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.Payload;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.finance.common.client.auth.AuthenticationClient;
import com.finance.common.constants.CommonHeaders;
import com.finance.common.constants.UserStatus;

import java.util.Optional;

import org.springframework.http.HttpHeaders;
import org.springframework.http.server.PathContainer;
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
    private final AuthenticationClient authenticationClient;
    private final ObjectMapper objectMapper;

    @Override
    public Mono<AuthorizationDecision> check(final Mono<Authentication> authentication, final AuthorizationContext object) {
        return authentication
            .flatMap(a -> {
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
                return authenticationClient.getAuthentication(username.get());
            })
            .filter(authenticationDTO -> authenticationDTO.getWalletUser().getStatus() == UserStatus.ACTIVE)
            .map(authenticationResponse -> {

                final var request = object.getExchange().getRequest();

                final var path = PathContainer.parsePath(request.getPath().subPath(5).value());
                final var httpMethod = request.getMethod();

                if (authenticationResponse.getAccessUris() == null || authenticationResponse.getAccessUris().isEmpty()) {
                    return new AuthorizationDecision(false);
                }

                final var pathPatternAccessUris = authenticationResponse.getAccessUris()
                    .stream()
                    .map(accessUri ->
                        new PathPatternAccessUri(PathPatternParser.defaultInstance.parse(accessUri.getUri()), accessUri.getMethodType()))
                    .toList();

                for (var p : pathPatternAccessUris) {
                    if (p.pathPattern.matches(path) && httpMethod.matches(p.httpMethod)) {

                        String walletUser;
                        try {
                            walletUser = objectMapper.writeValueAsString(authenticationResponse.getWalletUser());
                        } catch (JsonProcessingException e) {
                            return new AuthorizationDecision(false);
                        }

                        request.mutate()
                            .header(CommonHeaders.X_USER, walletUser)
                            .headers(h -> h.remove(HttpHeaders.AUTHORIZATION))
                            .build();

                        return new AuthorizationDecision(true);
                    }
                }

                return new AuthorizationDecision(false);
            })
            .defaultIfEmpty(new AuthorizationDecision(false));
    }

    private record PathPatternAccessUri(PathPattern pathPattern, String httpMethod) {
    }
}
