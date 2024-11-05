package com.finance.wallet.user.exception;

import com.finance.common.exception.ApplicationError;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum UserServiceError implements ApplicationError {

    USERNAME_ALREADY_EXISTS("USER_001_USERNAME_ALREADY_EXISTS"),
    EMAIL_ALREADY_EXISTS("USER_002_EMAIL_ALREADY_EXISTS"),
    INVALID_ALGORITHM("USER_003_INVALID_ALGORITHM");

    private final String errorCode;
}
