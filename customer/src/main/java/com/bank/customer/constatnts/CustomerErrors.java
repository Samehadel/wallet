package com.bank.customer.constatnts;


import com.finance.common.exception.ApplicationError;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum CustomerErrors implements ApplicationError {
    REQUIRED_CUSTOMER_CODE("CUS_REQUIRED_CUSTOMER_CODE"),
    CUSTOMER_NOT_FOUND("CUS_CUSTOMER_NOT_FOUND"),
    EMAIL_ALREADY_EXISTS("CUS_EMAIL_ALREADY_EXISTS"),
    PHONE_NUMBER_ALREADY_EXISTS("CUS_PHONE_NUMBER_ALREADY_EXISTS"),
    OFFICIAL_ID_ALREADY_EXISTS("CUS_OFFICIAL_ID_ALREADY_EXISTS");

    private final String errorCode;
}
