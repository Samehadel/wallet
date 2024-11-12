package com.finance.gateway;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.finance.common.dto.AccessUri;
import com.finance.common.dto.AuthenticationDTO;
import com.finance.common.util.CollectionUtil;
import com.finance.gateway.dto.GwAuthorizationContext;
import com.finance.gateway.dto.PathPatternAccessUri;

import java.util.List;

import org.springframework.http.server.PathContainer;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.pattern.PathPatternParser;

import reactor.core.publisher.Mono;

@Service
public class AuthenticationService {
    private final WebClient webClient;
    private final ObjectMapper objectMapper;

    public AuthenticationService(final WebClient.Builder webClientBuilder, final ObjectMapper objectMapper) {
        this.webClient = webClientBuilder.baseUrl("http://user-svc").build();
        this.objectMapper = objectMapper;
    }

    public GwAuthorizationContext getAuthenticationContext(AuthenticationDTO authenticationDTO, final ServerHttpRequest request) {

        final var path = PathContainer.parsePath(request.getPath().subPath(5).value());
        final var httpMethod = request.getMethod();

        List<AccessUri> accessUris = authenticationDTO.getAccessUris();
        if (CollectionUtil.isNullOrEmpty(accessUris)) {
            return buildUnAuthorizedResponse();
        }

        final var pathPatternAccessUris = accessUris
            .stream()
            .map(accessUri ->
                new PathPatternAccessUri(PathPatternParser.defaultInstance.parse(accessUri.getUri()), accessUri.getMethodType()))
            .toList();

        for (var p : pathPatternAccessUris) {
            if (p.pathPattern().matches(path) && httpMethod.matches(p.httpMethod())) {

                String walletUser;
                try {
                    walletUser = objectMapper.writeValueAsString(authenticationDTO.getWalletUser());
                } catch (JsonProcessingException e) {
                    return buildUnAuthorizedResponse();
                }

                return buildAuthenticationContext(walletUser, true);
            }
        }

        return buildUnAuthorizedResponse();
    }

    private GwAuthorizationContext buildUnAuthorizedResponse() {
        return buildAuthenticationContext(null, false);
    }

    private GwAuthorizationContext buildAuthenticationContext(final String walletUserJson, boolean authorizationDecision) {
        return new GwAuthorizationContext(walletUserJson, new AuthorizationDecision(authorizationDecision));
    }

    public Mono<AuthenticationDTO> getAuthenticationCall(String username) {
        return webClient.get()
            .uri("/api/v1/user/username/{username}", username)
            .retrieve()
            .bodyToMono(AuthenticationDTO.class);
    }
}
