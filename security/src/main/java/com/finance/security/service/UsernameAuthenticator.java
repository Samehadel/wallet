package com.finance.security.service;

import com.finance.common.client.UserClient;
import com.finance.common.dto.AuthResultDTO;
import com.finance.common.dto.AuthenticationRequest;
import com.finance.common.exception.ExceptionService;
import com.finance.common.service.PasswordEncryptor;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UsernameAuthenticator implements Authenticator {
    private final UserClient userClient;
    private final PasswordEncryptor passwordEncryptor;
    private final ExceptionService exceptionService;

    @Override
    public AuthResultDTO authenticate(final AuthenticationRequest authenticationRequest) {
        return null;
    }
}
