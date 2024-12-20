package com.finance.gateway.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.finance.common.client.SecurityClient;
import com.finance.common.constants.AuthResultEnum;
import com.finance.common.constants.UrlMethodEnum;
import com.finance.common.dto.AuthResultDTO;
import com.finance.common.dto.AuthorizationRequest;
import com.finance.common.dto.UserDTO;
import com.finance.gateway.dto.GwAuthorizationContext;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import lombok.extern.log4j.Log4j2;

import reactor.core.publisher.Mono;

@Service
@Log4j2
public class AuthenticationService {
    private final WebClient webClient;
    private final ObjectMapper objectMapper;
    private final SecurityClient securityClient;

    @Value("${security.svc.url}")
    private String securitySvcUrl;

    public AuthenticationService(final WebClient.Builder webClientBuilder, final ObjectMapper objectMapper, final SecurityClient securityClient) {
        this.webClient = webClientBuilder.baseUrl("http://security-svc").build();
        this.objectMapper = objectMapper;
        this.securityClient = securityClient;
    }

    public Mono<GwAuthorizationContext> getAuthenticationContext(final String token, final ServerHttpRequest request) {
        log.info("Authenticating user with token: [{}]", token);
        return Mono.just(buildAuthorizationRequest(token, request))
            .flatMap(this::authorize)
            .map(authResult -> {
                if (AuthResultEnum.AUTHORIZED == authResult.getAuthResult()) {
                    return buildAuthenticationContext(authResult.getUser(), true);
                }
                return buildUnAuthorizedResponse();
            })
            .onErrorResume(e -> {
                log.error("Failed to authenticate user: [{}]", e.getMessage());
                return Mono.just(buildUnAuthorizedResponse());
            });
    }

    private AuthorizationRequest buildAuthorizationRequest(final String token, final ServerHttpRequest request) {
        String requestPath = request.getPath().subPath(4).value();
        final var httpMethod = request.getMethod();
        UrlMethodEnum method = UrlMethodEnum.valueOf(httpMethod.name());
        return buildAuthorizationRequest(token, requestPath, method);
    }

    private AuthorizationRequest buildAuthorizationRequest(final String token, final String requestPath, final UrlMethodEnum method) {
        AuthorizationRequest authorizationRequest = new AuthorizationRequest();
        authorizationRequest.setToken(token);
        authorizationRequest.setMethod(method);
        authorizationRequest.setUrl(requestPath);

        return authorizationRequest;
    }

    private Mono<AuthResultDTO> authorize(final AuthorizationRequest username) {
        return webClient.post()
            .uri(securitySvcUrl, username)
            .retrieve()
            .bodyToMono(AuthResultDTO.class);

    }

    private GwAuthorizationContext buildAuthenticationContext(final UserDTO user, final boolean authorizationDecision) {
        final String userJson = user != null ? user.toString() : null;
        return new GwAuthorizationContext(userJson, new AuthorizationDecision(authorizationDecision));
    }

    private GwAuthorizationContext buildUnAuthorizedResponse() {
        return buildAuthenticationContext(null, false);
    }

}
