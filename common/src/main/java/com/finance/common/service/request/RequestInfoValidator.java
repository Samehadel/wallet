package com.finance.common.service.request;

import com.finance.common.exception.ExceptionService;
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
            return;
        }

        String requestUsername = requestInfoService.getRequestUser().getUsername();
        if (!ObjectUtils.equals(expectedUsername, requestUsername)) {
            throw exceptionService.buildUnauthorizedException();
        }
    }

    public void validateByMobiles(final String expectedMobile) {
        if (requestInfoService.getRequestUser() == null) {
            return;
        }

        String requestMobile = requestInfoService.getRequestUser().getMobile();
        if (!ObjectUtils.equals(expectedMobile, requestMobile)) {
            throw exceptionService.buildUnauthorizedException();
        }
    }
}
