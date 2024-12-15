package com.finance.security.service.token;

import com.finance.common.util.ObjectUtils;

import java.time.LocalDateTime;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UserTokenHolder {
    private final UserToken userToken;
    private final long maxIdleTimeSeconds;

    public boolean expired() {
        return !ObjectUtils.isNull(userToken)
            && userToken.getExpirationTime() != null
            && userToken.getExpirationTime().isBefore(LocalDateTime.now());
    }

    public boolean sameToken(final String token) {
        return !ObjectUtils.isNull(userToken)
            && userToken.getToken() != null
            && userToken.getToken().equals(token);
    }

    public boolean idle() {
        return !ObjectUtils.isNull(userToken)
            && userToken.getLastAccessTime() != null
            && idleTimeApproached(maxIdleTimeSeconds);
    }

    private boolean idleTimeApproached(final long maxIdleSeconds) {
        return userToken.getLastAccessTime().plusSeconds(maxIdleSeconds).isBefore(LocalDateTime.now());
    }
}
