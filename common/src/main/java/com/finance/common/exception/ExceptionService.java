package com.finance.common.exception;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ExceptionService {
    private final MessageSource messageSource;

    public BadRequestException throwBadRequestException(final ApplicationError applicationError, final String... args) {
        ErrorDetails errorDetails = buildError(applicationError, args);

        return new BadRequestException(errorDetails);
    }

    public InternalException throwInternalException(final ApplicationError applicationError, final String... args) {
        ErrorDetails errorDetails = buildError(applicationError, args);

        return new InternalException(errorDetails);
    }

    private ErrorDetails buildError(final ApplicationError applicationError, final String[] args) {
        String errorMessage = messageSource.getMessage(applicationError.getErrorCode(), args, LocaleContextHolder.getLocale());

        return new ErrorDetails(applicationError.getErrorCode(), errorMessage);
    }
}
