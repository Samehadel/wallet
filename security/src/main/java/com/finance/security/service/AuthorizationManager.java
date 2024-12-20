package com.finance.security.service;

import com.finance.common.constants.AuthResultEnum;
import com.finance.common.dto.AuthResultDTO;
import com.finance.common.dto.AuthorizationRequest;
import com.finance.common.exception.ExceptionService;
import com.finance.common.exception.SharedApplicationError;
import com.finance.common.util.ObjectUtils;
import com.finance.common.util.StringUtil;
import com.finance.security.exception.SecurityServiceError;
import com.finance.security.service.token.UserTokenFactory;
import com.finance.security.service.token.UserTokenHolder;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
@RequiredArgsConstructor
public class AuthorizationManager {
    private final TokenService tokenService;
    private final UserTokenFactory userTokenFactory;
    private final ExceptionService exceptionService;
    private final UserAccessService userAccessService;

    public AuthResultDTO authorize(final AuthorizationRequest authRequest) {
        validateRequiredFields(authRequest);
        if (userAccessService.isPublicEndpoint(authRequest.getUrl(), authRequest.getMethod())) {
            return buildAuthResult(AuthResultEnum.AUTHORIZED);
        }

        String username = parseTokenForUsername(authRequest.getToken());
        UserTokenHolder userTokenHolder = userTokenFactory.getUserTokenHolder(username);
        validateToken(userTokenHolder, authRequest.getToken());

        return userAccessService.userHasAccess(username, authRequest.getUrl(), authRequest.getMethod())
            ? buildAuthResult(AuthResultEnum.AUTHORIZED)
            : buildAuthResult(AuthResultEnum.UNAUTHORIZED);
    }

    private String parseTokenForUsername(final String token) {
        String username = tokenService.parseForUsername(token);
        if (StringUtil.isNullOrEmpty(username)) {
            log.error("Failed to parse username from token: [{}]", token);
            throw exceptionService.buildInternalExceptionWithReference(SharedApplicationError.GENERIC_ERROR);
        }

        return username;
    }

    private void validateRequiredFields(final AuthorizationRequest authRequest) {
        if (ObjectUtils.anyNull(authRequest, authRequest.getMethod(), authRequest.getUrl(), authRequest.getToken())) {
            log.error("Missing required fields in AuthorizationRequest: [{}]", authRequest);
            throw new IllegalArgumentException("Missing required fields in AuthorizationRequest");
        }
    }

    private void validateToken(final UserTokenHolder userTokenHolder, final String requestToken) {
        if (userTokenHolder == null) {
            log.error("UserTokenHolder is null");
            throw exceptionService.buildInternalExceptionWithReference(SharedApplicationError.GENERIC_ERROR);
        }

        if (userTokenHolder.expired()) {
            log.error("Token is expired");
            throw exceptionService.buildUnauthorizedException(SecurityServiceError.TOKEN_EXPIRED);
        }

        if (userTokenHolder.sameToken(requestToken)) {
            log.error("Token mismatch");
            throw exceptionService.buildUnauthorizedException(SecurityServiceError.TOKEN_MISMATCH);
        }

        if (userTokenHolder.idle()) {
            log.error("Token is idle");
            throw exceptionService.buildUnauthorizedException(SecurityServiceError.TOKEN_IDLE);
        }
    }

    private AuthResultDTO buildAuthResult(final AuthResultEnum authResult) {
        return AuthResultDTO.builder()
            .authResult(authResult)
            .build();
    }
}
