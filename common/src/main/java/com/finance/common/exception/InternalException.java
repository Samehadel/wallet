package com.finance.common.exception;

import org.springframework.http.HttpStatus;

public class InternalException extends ApplicationException {

    public InternalException(final ErrorDetails errorDetails) {
        super(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
