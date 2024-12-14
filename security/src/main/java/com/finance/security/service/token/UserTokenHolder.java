package com.finance.security.service.token;

import java.time.LocalDateTime;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UserTokenHolder {
    private final UserToken userToken;

    public boolean expired() {
        return userToken == null
            || userToken.getExpirationTime() == null
            || userToken.getExpirationTime().isBefore(LocalDateTime.now());
    }

    public boolean sameToken(final String token) {
        return userToken != null
            && userToken.getToken() != null
            && userToken.getToken().equals(token);
    }

    public boolean idle(final int maxIdleSeconds) {
        return userToken != null
            && userToken.getLastAccessTime() != null
            && idleTimeApproached(maxIdleSeconds);
    }

    private boolean idleTimeApproached(final int maxIdleSeconds) {
        return userToken.getLastAccessTime().plusSeconds(maxIdleSeconds).isBefore(LocalDateTime.now());
    }
}
