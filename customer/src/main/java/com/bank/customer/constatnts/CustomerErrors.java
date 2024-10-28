package com.bank.customer.constatnts;

import com.finance.common.exception.ApplicationErrorInterface;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum CustomerErrors implements ApplicationErrorInterface {
    REQUIRED_CUSTOMER_CODE("CUS_001", "Customer code is required"),
    CUSTOMER_NOT_FOUND("CUS_002", "Customer not found with code [%s]"),;

    private final String code;

    private final String message;
}
