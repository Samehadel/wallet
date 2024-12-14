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
        log.info("Creating new user token");
        validateRequiredFields(user);
        UserToken userToken = UserToken.builder()
            .token(tokenService.generateToken(user.getUsername()))
            .userId(user.getId())
            .cif(user.getCif())
            .lastAccessTime(LocalDateTime.now())
            .expirationTime(LocalDateTime.now().plusSeconds(jwtExpirationSec))
            .build();

        cacheUserToken(user.getUsername(), userToken);
        log.info("User token created and cached successfully");

        return userToken;
    }

    private void validateRequiredFields(final UserDTO user) {
        if (ObjectUtils.anyNull(user.getUsername(), user.getId(), user.getCif())) {
            throw exceptionService.buildInternalException(SharedApplicationError.MISSING_REQUIRED_FIELD);
        }
    }

    private void cacheUserToken(final String username, final UserToken userToken) {
        log.info("Caching user token for user [{}]", username);
        CacheService<UserToken> tokenCacheService = cacheServiceFactory.buildCacheInstance(userTokenCacheName, UserToken.class);
        tokenCacheService.cache(username, userToken);
    }

    public UserTokenHolder getUserToken(final String username) {
        log.info("Getting user token for user [{}]", username);
        UserToken userToken = getUserTokenFromCache(username);
        return new UserTokenHolder(userToken);
    }

    private UserToken getUserTokenFromCache(final String username) {
        log.info("Getting user token from cache for user [{}]", username);
        CacheService<UserToken> tokenCacheService = cacheServiceFactory.buildCacheInstance(userTokenCacheName, UserToken.class);
        return tokenCacheService.get(username);
    }
}
