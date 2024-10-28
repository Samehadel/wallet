package com.finance.common.exception;

import org.springframework.stereotype.Service;

@Service
public class ExceptionService {

    public void throwBadRequestException(final ApplicationErrorInterface applicationError, final String... args) {
        throw new ApplicationBadRequestException(applicationError, args);
    }

    public void throwLogicalException(final ApplicationErrorInterface applicationError, final String... args) {
        throw new ApplicationLogicalException(applicationError, args);
    }

    public <E extends ApplicationException> E buildException(final ApplicationErrorInterface applicationError, final Class<E> exceptionClass,
        final String... args) {
        if (ApplicationBadRequestException.class.equals(exceptionClass)) {
            throw new ApplicationBadRequestException(applicationError, args);
        } else if (ApplicationLogicalException.class.equals(exceptionClass)) {
            throw new ApplicationLogicalException(applicationError, args);
        } else {
            throw new IllegalArgumentException("Invalid exception class");
        }
    }
}
