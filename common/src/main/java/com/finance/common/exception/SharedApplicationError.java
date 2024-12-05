package com.finance.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum SharedApplicationError implements ApplicationError {

    GENERIC_ERROR("SH_0001_GENERAL_ERROR"),
    FORBIDDEN_ERROR("SH_0002_FORBIDDEN_ERROR"),
    UNAUTHORIZED_ERROR("SH_0003_UNAUTHORIZED_ERROR"),
    MISSING_REQUIRED_FIELD("SH_0004_MISSING_REQUIRED_FIELD"),
    INVALID_EMAIL("SH_0005_INVALID_EMAIL"),
    INVALID_PHONE_NUMBER("SH_0006_INVALID_PHONE_NUMBER"),
    INVALID_NATIONAL_ID("SH_0007_INVALID_NATIONAL_ID"),
    VALIDATION_ERROR("SH_0008_VALIDATION_ERROR"),;

    private final String errorCode;
}
