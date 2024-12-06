package com.finance.wallet.user.exception;

import com.finance.common.exception.ApplicationError;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum UserServiceError implements ApplicationError {

    USERNAME_ALREADY_EXISTS("USER_001_USERNAME_ALREADY_EXISTS"),
    MOBILE_ALREADY_EXISTS("USER_002_MOBILE_ALREADY_EXISTS"),
    MOBILE_REQUIRED("USER_005_MOBILE_REQUIRED"),;

    private final String errorCode;
}
