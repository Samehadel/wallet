package com.finance.security.service;

import com.finance.common.client.UserClient;
import com.finance.common.dto.AuthenticationRequest;
import com.finance.common.exception.ExceptionService;
import com.finance.common.service.PasswordEncryptor;
import com.finance.common.util.StringUtil;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthenticatorFactory {
    private final UserClient userClient;
    private final PasswordEncryptor passwordEncryptor;
    private final ExceptionService exceptionService;

    private final Map<String, Authenticator> authenticatorMap = new HashMap<>();

    public Authenticator getAuthenticator(final AuthenticationRequest request) {
        if (StringUtil.isNullOrEmpty(request.getMobile())) {
            return authenticatorMap.computeIfAbsent("username_authenticator", k -> new UsernameAuthenticator(userClient, passwordEncryptor, exceptionService));
        } else {
            return authenticatorMap.computeIfAbsent("mobile_authenticator", k -> new MobileAuthenticator(userClient, passwordEncryptor, exceptionService));
        }
    }
}
