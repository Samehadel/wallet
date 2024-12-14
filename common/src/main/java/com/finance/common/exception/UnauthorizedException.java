package com.finance.common.exception;

import org.springframework.http.HttpStatus;

public class UnauthorizedException extends ApplicationException {

    protected UnauthorizedException(final ErrorDetails errorDetails) {
        super(errorDetails, HttpStatus.UNAUTHORIZED);
    }
}
