package com.finance.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class SharedApplicationError implements ApplicationErrorInterface {

    public final static SharedApplicationError GENERIC_ERROR = new SharedApplicationError("SH_0001", "General Error");
    public final static SharedApplicationError FORBIDDEN_ERROR = new SharedApplicationError("SH_0002", "Access Forbidden");
    public final static SharedApplicationError UNAUTHORIZED_ERROR = new SharedApplicationError("SH_0003", "Access Unauthorized");

    public final static SharedApplicationError MISSING_REQUIRED_FIELD = new SharedApplicationError("SH_0004", "Missing Required Field [%s]");
    public final static SharedApplicationError INVALID_EMAIL = new SharedApplicationError("SH_0005", "Invalid Email");
    public final static SharedApplicationError INVALID_PHONE_NUMBER = new SharedApplicationError("SH_0006", "Invalid Phone Number");
    public final static SharedApplicationError INVALID_NATIONAL_ID = new SharedApplicationError("SH_0007", "Invalid National ID");
    public final static SharedApplicationError INVALID_PASSPORT_NUMBER = new SharedApplicationError("SH_0008", "Invalid Passport Number");
    public final static SharedApplicationError PHONE_NUMBER_ALREADY_EXISTS = new SharedApplicationError("SH_0009", "Phone number already exists");
    public final static SharedApplicationError OFFICIAL_ID_ALREADY_EXISTS = new SharedApplicationError("SH_0010", "Official ID with same type "
        + "already exists");

    private final String code;
    private final String message;
}
