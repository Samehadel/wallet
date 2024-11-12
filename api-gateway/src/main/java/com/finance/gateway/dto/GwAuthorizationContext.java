package com.finance.gateway.dto;

import org.springframework.security.authorization.AuthorizationDecision;

public record GwAuthorizationContext(String walletUser, AuthorizationDecision authorizationDecision) {
}
