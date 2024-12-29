package com.finance.security.service;

import com.finance.common.constants.AuthResultEnum;
import com.finance.common.dto.AuthResultDTO;
import com.finance.common.dto.AuthorizationRequest;
import com.finance.common.dto.UserDTO;
import com.finance.common.exception.ExceptionService;
import com.finance.common.exception.SharedApplicationError;
import com.finance.common.util.ObjectUtils;
import com.finance.common.util.StringUtil;
import com.finance.security.service.token.TokenValidationService;
import com.finance.security.service.token.UserTokenService;
import com.finance.security.service.token.UserTokenHolder;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
@RequiredArgsConstructor
public class AuthorizationService {
    private final UserTokenService userTokenService;
    private final UserAccessService userAccessService;
    private final TokenValidationService tokenValidationService;

    public AuthResultDTO authorize(final AuthorizationRequest authRequest) {
        validateRequiredFieldsNotNull(authRequest);
        UserTokenHolder userTokenHolder = userTokenService.getUserToken(authRequest.getToken());
        tokenValidationService.validateToken(userTokenHolder, authRequest.getToken());

        if (userAccessService.isPublicEndpoint(authRequest.getUrl(), authRequest.getMethod())) {
            return buildAuthorizedResult(userTokenHolder.buildUserFromToken());
        }

        AuthResultDTO authResult;
        if (userAccessService.userHasAccess(userTokenHolder.getUsername(), authRequest.getUrl(), authRequest.getMethod())) {
            authResult = buildAuthorizedResult(userTokenHolder.buildUserFromToken());
            userTokenService.updateLastAccessTime(userTokenHolder);
        } else {
            authResult = buildUnauthorizedResult();
        }

        return authResult;
    }

    private void validateRequiredFieldsNotNull(final AuthorizationRequest authRequest) {
        if (ObjectUtils.anyNull(authRequest, authRequest.getMethod(), authRequest.getUrl(), authRequest.getToken())) {
            log.error("Missing required fields in AuthorizationRequest: [{}]", authRequest);
            throw new IllegalArgumentException("Missing required fields in AuthorizationRequest");
        }
    }

    private AuthResultDTO buildAuthorizedResult(final UserDTO user) {
        return buildAuthResult(AuthResultEnum.AUTHORIZED, user);
    }

    private AuthResultDTO buildUnauthorizedResult() {
        return buildAuthResult(AuthResultEnum.UNAUTHORIZED, null);
    }
    private AuthResultDTO buildAuthResult(final AuthResultEnum authResult, final UserDTO user) {
        return AuthResultDTO.builder()
            .authResult(authResult)
            .user(user)
            .build();
    }
}
