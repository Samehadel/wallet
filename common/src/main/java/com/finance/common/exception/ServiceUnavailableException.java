package com.finance.common.exception;

import org.springframework.http.HttpStatus;

public class ServiceUnavailableException extends ApplicationException {

    protected ServiceUnavailableException(final ErrorDetails errorDetails) {
        super(errorDetails, HttpStatus.SERVICE_UNAVAILABLE);
    }
}
