package com.finance.common.exception;

import org.springframework.http.HttpStatus;

public class BadRequestException extends ApplicationException {

    public BadRequestException(final ErrorDetails errorDetails) {
        super(errorDetails, HttpStatus.BAD_REQUEST);
    }
}
