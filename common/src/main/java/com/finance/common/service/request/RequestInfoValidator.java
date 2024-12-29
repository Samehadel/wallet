package com.finance.common.service.request;

import com.finance.common.exception.ExceptionService;
import com.finance.common.exception.SharedApplicationError;
import com.finance.common.util.ObjectUtils;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public final class RequestInfoValidator {
    private final RequestInfoService requestInfoService;
    private final ExceptionService exceptionService;

    public void validateByUsernames(final String expectedUsername) {
        if (requestInfoService.getRequestUser() == null) {
            throw exceptionService.buildUnauthorizedException(SharedApplicationError.UNAUTHORIZED_USER_NOT_FOUND);
        }

        String requestUsername = requestInfoService.getRequestUser().getUsername();
        if (!ObjectUtils.equals(expectedUsername, requestUsername)) {
            throw exceptionService.buildUnauthorizedException(SharedApplicationError.UNAUTHORIZED_USERNAME_MISMATCH);
        }
    }
}
