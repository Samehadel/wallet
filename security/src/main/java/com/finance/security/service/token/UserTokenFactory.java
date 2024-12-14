package com.finance.security.service.token;

import com.finance.common.dto.UserDTO;
import com.finance.common.exception.ExceptionService;
import com.finance.common.exception.SharedApplicationError;
import com.finance.common.service.cache.CacheService;
import com.finance.common.service.cache.CacheServiceFactory;
import com.finance.common.util.ObjectUtils;
import com.finance.security.service.TokenService;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Service
@RequiredArgsConstructor
@Log4j2
public class UserTokenFactory {
    private final ExceptionService exceptionService;
    private final CacheServiceFactory cacheServiceFactory;
    private final TokenService tokenService;

    @Value("${jwt.expiration.sec:3600}")
    private Integer jwtExpirationSec;

    @Value("${redis.user.token.cache.name}")
    private String userTokenCacheName;

    public UserToken createUserToken(final UserDTO user) {
        log.info("Creating token for user [{}]", user.getUsername());
        validateRequiredFields(user);
        UserToken userToken = UserToken.builder()
            .token(tokenService.generateToken(user.getUsername()))
            .userId(user.getId())
            .cif(user.getCif())
            .lastAccessTime(LocalDateTime.now())
            .expirationTime(LocalDateTime.now().plusSeconds(jwtExpirationSec))
            .build();

        cacheUserToken(user.getUsername(), userToken);
        return userToken;
    }

    private void validateRequiredFields(final UserDTO user) {
        if (ObjectUtils.anyNull(user.getUsername(), user.getId(), user.getCif())) {
            throw exceptionService.buildInternalException(SharedApplicationError.MISSING_REQUIRED_FIELD);
        }
    }

    private void cacheUserToken(final String username, final UserToken userToken) {
        CacheService<UserToken> tokenCacheService = cacheServiceFactory.buildCacheInstance(userTokenCacheName, UserToken.class);
        tokenCacheService.cache(username, userToken);
    }

    public
}
