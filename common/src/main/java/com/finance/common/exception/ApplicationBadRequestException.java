package com.finance.common.exception;

import org.springframework.http.HttpStatus;

public class ApplicationBadRequestException extends ApplicationException {

    public ApplicationBadRequestException(final ApplicationErrorInterface applicationError, final String... args) {
        super(applicationError, args);
    }

    @Override
    public String getApplicationErrorCode() {
        return this.applicationError.getCode();
    }

    @Override
    public String getErrorMessage() {
        return this.errorMessage;
    }

    @Override
    public HttpStatus getHttpStatus() {
        return HttpStatus.INTERNAL_SERVER_ERROR;
    }
}
