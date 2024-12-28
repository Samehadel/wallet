package com.finance.security.service.token;

import com.finance.common.dto.UserDTO;
import com.finance.common.exception.ExceptionService;
import com.finance.common.exception.SharedApplicationError;
import com.finance.common.service.cache.CacheOperationFactory;
import com.finance.common.service.cache.CacheOperationService;
import com.finance.common.util.ObjectUtils;
import com.finance.security.service.TokenService;

import java.time.LocalDateTime;

import jakarta.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
@RequiredArgsConstructor
public class UserTokenFactory {
    private final ExceptionService exceptionService;
    private final TokenService tokenService;
    private final CacheOperationFactory cacheOperationFactory;

    private CacheOperationService<UserToken> cacheOperationService;

    @Value("${redis.user.token.cache.name:user-token-cache}")
    private String userTokenCacheName;

    @Value("${user.token.expiration.sec:3600}")
    private long jwtExpirationSec;

    @Value("${user.token.max.idle.time.sec:300}")
    private long maxIdleTimeSeconds;

    @PostConstruct
    public void initializeCacheOperation() {
        this.cacheOperationService = cacheOperationFactory.createInstance(userTokenCacheName, UserToken.class, jwtExpirationSec);
    }

    public UserToken createUserToken(final UserDTO user) {
        log.info("Creating new user token");
        validateRequiredFields(user);
        UserToken userToken = UserToken.builder()
            .token(tokenService.generateToken(user.getUsername()))
            .userId(user.getId())
            .cif(user.getCif())
            .username(user.getUsername())
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
        cacheOperationService.cache(username, userToken);
    }

    public UserTokenHolder getUserTokenHolder(final String username) {
        log.info("Getting user token for user [{}]", username);
        UserToken userToken = getUserTokenFromCache(username);
        return new UserTokenHolder(userToken, maxIdleTimeSeconds);
    }

    private UserToken getUserTokenFromCache(final String username) {
        return cacheOperationService.get(username);
    }

    public void sync(final UserTokenHolder userTokenHolder) {
        final UserToken userToken = userTokenHolder.getUserToken();
        cacheOperationService.cache(userToken.getUsername(), userTokenHolder.getUserToken());
    }
}
