package com.finance.common.exception;

import org.springframework.http.HttpStatus;

public class BadRequestException extends ApplicationException {

    protected BadRequestException(final ErrorDetails errorDetails) {
        super(errorDetails, HttpStatus.BAD_REQUEST);
    }
}
