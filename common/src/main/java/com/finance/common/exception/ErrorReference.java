package com.finance.common.exception;

import com.finance.common.service.ErrorReferenceGenerator;

public class ErrorReference {
    private String service;
    private String reference;

    public ErrorReference(final String service) {
        this.service = service;
        this.reference = ErrorReferenceGenerator.generateErrorReverence();
    }

    public String getErrorReference() {
        return service + "-" + reference;
    }

    @Override
    public String toString() {
        return getErrorReference();
    }
}
