package com.finance.common.service;

public interface LocalizedMessageSource {

    String getLocalizedMessage(String errorCode, String[] args);
}
