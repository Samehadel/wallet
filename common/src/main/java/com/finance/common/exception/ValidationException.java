package com.finance.common.exception;

import org.springframework.http.HttpStatus;

public class ValidationException extends ApplicationException {
    ValidationException(final ErrorDetails errorDetails) {
        super(errorDetails, HttpStatus.BAD_REQUEST);
    }
}
