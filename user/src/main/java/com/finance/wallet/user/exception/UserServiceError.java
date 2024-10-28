package com.finance.wallet.user.exception;

import com.finance.common.exception.ApplicationErrorInterface;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class UserServiceError implements ApplicationErrorInterface {

    public final static UserServiceError USERNAME_ALREADY_EXISTS = new UserServiceError("USER_001", "Username already exists");
    public final static UserServiceError EMAIL_ALREADY_EXISTS = new UserServiceError("USER_002", "Email already exists");
    public static final ApplicationErrorInterface INVALID_ALGORITHM = new UserServiceError("USER_003", "Invalid Algorithm");

    private final String code;
    private final String message;
}
