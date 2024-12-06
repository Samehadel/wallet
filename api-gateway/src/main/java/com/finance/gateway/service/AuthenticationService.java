package com.finance.gateway.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.finance.common.constants.UserStatusEnum;
import com.finance.common.dto.AccessUriDTO;
import com.finance.common.dto.AuthResultDTO;
import com.finance.common.util.CollectionUtil;
import com.finance.gateway.dto.GwAuthorizationContext;
import com.finance.gateway.dto.PathPatternAccessUri;

import java.util.List;

import jakarta.ws.rs.NotAllowedException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.server.PathContainer;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.pattern.PathPatternParser;

import lombok.extern.log4j.Log4j2;

import reactor.core.publisher.Mono;

@Service
@Log4j2
public class AuthenticationService {
    private final WebClient webClient;
    private final ObjectMapper objectMapper;

    @Value("${user.svc.url}")
    private String userSvcUrl;

    public AuthenticationService(final WebClient.Builder webClientBuilder, final ObjectMapper objectMapper) {
        this.webClient = webClientBuilder.baseUrl("http://user-svc").build();
        this.objectMapper = objectMapper;
    }

    public GwAuthorizationContext getAuthenticationContext(final AuthResultDTO authResult, final ServerHttpRequest request) {
        log.info("Getting authentication context");
        try {
            verifyActiveUser(authResult);

            if (userHasAccess(authResult.getAccessUris(), request)) {
                String walletUserJson = objectMapper.writeValueAsString(authResult.getUser());
                return buildAuthenticationContext(walletUserJson, true);
            }

            return buildUnAuthorizedResponse();
        } catch (Exception e) {
            log.error("Failed to authenticate user: [{}]", e.getMessage());
            return buildUnAuthorizedResponse();
        }
    }

    private void verifyActiveUser(final AuthResultDTO authResult) {
        if (authResult == null
            || authResult.getUser() == null
            || authResult.getUser().getStatus() != UserStatusEnum.ACTIVE) {

            throw new NotAllowedException("User is not active");
        }
    }

    private boolean userHasAccess(final List<AccessUriDTO> accessUris, final ServerHttpRequest request) {
        final var path = PathContainer.parsePath(request.getPath().subPath(5).value());
        final var httpMethod = request.getMethod();

        if (CollectionUtil.isNullOrEmpty(accessUris)) {
            return false;
        }

        final var pathPatternAccessUris = convertToPathPattern(accessUris);

        return pathPatternAccessUris
            .stream()
            .anyMatch(p -> p.pathPattern().matches(path) && httpMethod.matches(p.httpMethod()));
    }

    private GwAuthorizationContext buildUnAuthorizedResponse() {
        return buildAuthenticationContext(null, false);
    }

    private List<PathPatternAccessUri> convertToPathPattern(final List<AccessUriDTO> accessUris) {
        return accessUris
            .stream()
            .map(accessUri ->
                new PathPatternAccessUri(PathPatternParser.defaultInstance.parse(accessUri.getUrl()), accessUri.getMethod().getName()))
            .toList();
    }

    private GwAuthorizationContext buildAuthenticationContext(final String walletUserJson, final boolean authorizationDecision) {
        return new GwAuthorizationContext(walletUserJson, new AuthorizationDecision(authorizationDecision));
    }

    public Mono<AuthResultDTO> getAuthenticationCall(final String username) {
        return webClient.get()
            .uri(userSvcUrl, username)
            .retrieve()
            .bodyToMono(AuthResultDTO.class);
    }
}
