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

    public void validateByUsername(final String inputUsername) {
        String requestUsername = requestInfoService.getRequestUsername();
        validateObjectsMatch(requestUsername, inputUsername);
    }
    
    public void validateByMobile(final String inputMobile) {
        String requestMobile = requestInfoService.getRequestMobile();
        validateObjectsMatch(requestMobile, inputMobile);
    }

    public void validateByCif(final String inputCif) {
        String requestCif = requestInfoService.getRequestCif();
        validateObjectsMatch(requestCif, inputCif);
    }
    
    private void validateObjectsMatch(final Object expected, final Object actual) {
        boolean objectsMatch = skipValidation() || ObjectUtils.equals(expected, actual);

        if (!objectsMatch) {
            throw exceptionService.buildUnauthorizedException(SharedApplicationError.UNAUTHORIZED_DATA_ACCESS);
        }
    }

    private boolean skipValidation() {
        return requestInfoService.internalRequest();
    }
}
