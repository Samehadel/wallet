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
    private final MobileAuthenticator mobileAuthenticator;
    private final UsernameAuthenticator usernameAuthenticator;

    public Authenticator getAuthenticator(final AuthenticationRequest request) {
        if (StringUtil.isNullOrEmpty(request.getMobile())) {
            return usernameAuthenticator;
        } else {
            return mobileAuthenticator;
        }
    }
}
