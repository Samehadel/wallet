package com.finance.security.service.token;

import com.finance.common.exception.ExceptionService;
import com.finance.common.exception.SharedApplicationError;
import com.finance.security.exception.SecurityServiceError;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Service
@RequiredArgsConstructor
@Log4j2
public class TokenValidationService {
    private final ExceptionService exceptionService;

    @Value("${user.token.max.idle.time.sec:300}")
    private long maxIdleTimeSeconds;

    public void validateToken(final UserTokenHolder userTokenHolder, final String requestToken) {
        if (userTokenHolder == null) {
            log.error("User token not found");
            throw exceptionService.buildInternalExceptionWithReference(SharedApplicationError.GENERIC_ERROR);
        }

        if (userTokenHolder.expired()) {
            log.error("Token is expired");
            throw exceptionService.buildUnauthorizedException(SecurityServiceError.TOKEN_EXPIRED);
        }

        if (userTokenHolder.tokenNotEqual(requestToken)) {
            log.error("Token mismatch");
            throw exceptionService.buildUnauthorizedException(SecurityServiceError.TOKEN_MISMATCH);
        }

        if (userTokenHolder.idle(maxIdleTimeSeconds)) {
            log.error("Token is idle");
            throw exceptionService.buildUnauthorizedException(SecurityServiceError.TOKEN_IDLE);
        }
    }
}
