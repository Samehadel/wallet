package com.finance.common.exception;


import org.springframework.http.HttpStatus;

public class ApplicationLogicalException extends ApplicationException {

    public ApplicationLogicalException(final ApplicationErrorInterface applicationError, final String... args) {
        super(applicationError, args);
    }

    @Override
    public String getErrorMessage() {
        return this.errorMessage;
    }

    @Override
    public HttpStatus getHttpStatus() {
        return HttpStatus.BAD_REQUEST;
    }

    @Override
    public String getApplicationErrorCode() {
        return applicationError.getCode();
    }
}
