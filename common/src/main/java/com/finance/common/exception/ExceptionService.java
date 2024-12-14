package com.finance.common.exception;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ExceptionService {
    private final MessageSource messageSource;

    @Value("${spring.application.name}")
    private String serviceName;

    public BadRequestException buildBadRequestException(final ApplicationError applicationError, final String... args) {
        ErrorDetails errorDetails = buildErrorDetails(applicationError, args);

        return new BadRequestException(errorDetails);
    }

    public BadRequestException buildBadExceptionWithReference(final ApplicationError applicationError, final String ... args) {
        ErrorDetails errorDetails = buildErrorDetails(applicationError, new ErrorReference(serviceName), args);

        return new BadRequestException(errorDetails);
    }

    public InternalException buildInternalException(final ApplicationError applicationError, final String... args) {
        ErrorDetails errorDetails = buildErrorDetails(applicationError, args);

        return new InternalException(errorDetails);
    }

    public InternalException buildInternalExceptionWithReference(final ApplicationError applicationError, final String... args) {
        ErrorDetails errorDetails = buildErrorDetails(applicationError, new ErrorReference(serviceName), args);

        return new InternalException(errorDetails);
    }

    public UnauthorizedException buildUnauthorizedException() {
        ErrorDetails errorDetails = buildErrorDetails(SharedApplicationError.UNAUTHORIZED, null);

        return new UnauthorizedException(errorDetails);
    }

    public ErrorDetails buildErrorDetails(final ApplicationError applicationError, final String[] args) {
        return buildErrorDetails(applicationError, null, args);
    }

    private ErrorDetails buildErrorDetails(final ApplicationError applicationError, final ErrorReference errorReference, final String[] args) {
        String errorMessage = messageSource.getMessage(applicationError.getErrorCode(), args, LocaleContextHolder.getLocale());

        if (errorReference != null) {
            errorMessage = errorMessage + " " + errorReference.getErrorReference();
        }

        return ErrorDetails.builder()
            .errorCode(applicationError.getErrorCode())
            .errorMessage(errorMessage)
            .build();
    }
}
