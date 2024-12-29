package com.finance.security.service.token;

import com.finance.common.dto.UserDTO;
import com.finance.common.util.ObjectUtils;
import com.finance.common.util.StringUtil;

import java.time.LocalDateTime;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UserTokenHolder {
    private final UserToken userToken;

    public boolean expired() {
        return !ObjectUtils.isNull(userToken)
            && userToken.getExpirationTime() != null
            && userToken.getExpirationTime().isBefore(LocalDateTime.now());
    }

    public boolean tokenNotEqual(final String token) {
        return StringUtil.isNullOrEmpty(token)
            || userToken == null
            || !token.equals(userToken.getToken());
    }

    public boolean idle(final long maxIdleTimeSeconds) {
        return !ObjectUtils.isNull(userToken)
            && userToken.getLastAccessTime() != null
            && idleTimeApproached(maxIdleTimeSeconds);
    }

    private boolean idleTimeApproached(final long maxIdleSeconds) {
        return userToken.getLastAccessTime().plusSeconds(maxIdleSeconds).isBefore(LocalDateTime.now());
    }

    public void updateLastAccessTime() {
        userToken.setLastAccessTime(LocalDateTime.now());
    }

    public String getUsername() {
        return userToken.getUsername();
    }

    public UserDTO buildUserFromToken() {
        return UserDTO.builder()
            .id(userToken.getUserId())
            .cif(userToken.getCif())
            .username(userToken.getUsername())
            .build();
    }

    UserToken getUserToken() {
        return userToken;
    }
}
