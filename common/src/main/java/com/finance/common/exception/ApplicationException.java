package com.finance.common.exception;

import org.springframework.http.HttpStatus;

public abstract class ApplicationException extends RuntimeException {
    protected ApplicationErrorInterface applicationError;
    protected String[] args;
    protected String errorMessage;

    ApplicationException (final ApplicationErrorInterface applicationError, final String... args) {
        super(applicationError.getMessage());
        this.errorMessage = this.getFormattedMessage(applicationError.getMessage(), args);
        this.applicationError = applicationError;
    }

    private String getFormattedMessage(final String message, final String... args) {
        return String.format(message, args);
    }

    public abstract String getApplicationErrorCode();

    public abstract String getErrorMessage();

    public abstract HttpStatus getHttpStatus();
}
