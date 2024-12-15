package com.finance.security.exception;

import com.finance.common.exception.ApplicationError;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum SecurityServiceError implements ApplicationError {
    TOKEN_EXPIRED("SEC_001_TOKEN_EXPIRED"),
    TOKEN_MISMATCH("SEC_002_TOKEN_MISMATCH"),
    TOKEN_IDLE("SEC_003_TOKEN_IDLE"),;


    private final String errorCode;
}
