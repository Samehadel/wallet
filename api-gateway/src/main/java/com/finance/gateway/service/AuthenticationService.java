package com.finance.gateway.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.finance.common.constants.UserStatus;
import com.finance.common.dto.AccessUri;
import com.finance.common.dto.AuthenticationDTO;
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

    public GwAuthorizationContext getAuthenticationContext(final AuthenticationDTO authenticationDTO, final ServerHttpRequest request) {
        log.info("Getting authentication context");
        try {
            verifyActiveUser(authenticationDTO);

            if (userHasAccess(authenticationDTO.getAccessUris(), request)) {
                String walletUserJson = objectMapper.writeValueAsString(authenticationDTO.getWalletUser());
                return buildAuthenticationContext(walletUserJson, true);
            }

            return buildUnAuthorizedResponse();
        } catch (Exception e) {
            log.error("Failed to authenticate user: [{}]", e.getMessage());
            return buildUnAuthorizedResponse();
        }
    }

    private void verifyActiveUser(final AuthenticationDTO authenticationDTO) {
        if (authenticationDTO == null
            || authenticationDTO.getWalletUser() == null
            || authenticationDTO.getWalletUser().getStatus() != UserStatus.ACTIVE) {

            throw new NotAllowedException("User is not active");
        }
    }

    private boolean userHasAccess(final List<AccessUri> accessUris, final ServerHttpRequest request) {
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

    private List<PathPatternAccessUri> convertToPathPattern(final List<AccessUri> accessUris) {
        return accessUris
            .stream()
            .map(accessUri ->
                new PathPatternAccessUri(PathPatternParser.defaultInstance.parse(accessUri.getUri()), accessUri.getMethodType()))
            .toList();
    }

    private GwAuthorizationContext buildAuthenticationContext(final String walletUserJson, final boolean authorizationDecision) {
        return new GwAuthorizationContext(walletUserJson, new AuthorizationDecision(authorizationDecision));
    }

    public Mono<AuthenticationDTO> getAuthenticationCall(final String username) {
        return webClient.get()
            .uri(userSvcUrl, username)
            .retrieve()
            .bodyToMono(AuthenticationDTO.class);
    }
}
