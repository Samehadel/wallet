package com.finance.common.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public abstract class ApplicationException extends RuntimeException {
    private final ErrorDetails errorDetails;
    private final HttpStatus status;

    ApplicationException(final ErrorDetails errorDetails, final HttpStatus httpStatus) {
        super(errorDetails.errorMessage());
        this.errorDetails = errorDetails;
        this.status = httpStatus;
    }
}
