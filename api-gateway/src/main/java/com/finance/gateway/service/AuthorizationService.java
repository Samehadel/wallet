package com.finance.gateway.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.finance.common.constants.AuthResultEnum;
import com.finance.common.constants.UrlMethodEnum;
import com.finance.common.dto.AuthResultDTO;
import com.finance.common.dto.AuthorizationRequest;
import com.finance.common.dto.UserDTO;
import com.finance.gateway.dto.GwAuthorizationContext;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import lombok.extern.log4j.Log4j2;

import reactor.core.publisher.Mono;

@Service
@Log4j2
public class AuthorizationService {
    private final WebClient webClient;
    private final ObjectMapper objectMapper;

    @Value("${security.svc.url}")
    private String securitySvcUrl;

    public AuthorizationService(final WebClient.Builder webClientBuilder, final ObjectMapper objectMapper) {
        this.webClient = webClientBuilder.baseUrl("http://security-svc").build();
        this.objectMapper = objectMapper;
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

    private AuthorizationRequest buildAuthorizationRequest(final String token, final String requestPath, final UrlMethodEnum httpMethod) {
        AuthorizationRequest authorizationRequest = new AuthorizationRequest();
        authorizationRequest.setToken(token);
        authorizationRequest.setMethod(httpMethod);
        authorizationRequest.setUrl(requestPath);

        return authorizationRequest;
    }

    private Mono<AuthResultDTO> authorize(final AuthorizationRequest authorizationRequest) {
        log.info("Request body: {}", authorizationRequest);
        return webClient.post()
            .uri(securitySvcUrl)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(authorizationRequest)
            .retrieve()
            .bodyToMono(AuthResultDTO.class)
            .doOnNext(authResult -> log.info("Authorization result: [{}]", authResult))
            .doOnError(e -> log.error("Failed to authorize user: [{}]", e.getMessage()));

    }

    private GwAuthorizationContext buildAuthenticationContext(final UserDTO user, final boolean authorizationDecision) {
        try {
            final String userJson = user != null ? objectMapper.writeValueAsString(user) : null;
            return new GwAuthorizationContext(userJson, new AuthorizationDecision(authorizationDecision));
        } catch (JsonProcessingException e) {
            log.error("Failed to serialize user: [{}]", e.getMessage());
            return buildUnAuthorizedResponse();
        }
    }

    private GwAuthorizationContext buildUnAuthorizedResponse() {
        return buildAuthenticationContext(null, false);
    }

}
