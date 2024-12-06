package com.finance.security.service;

import com.finance.common.client.UserClient;
import com.finance.common.dto.AuthenticationRequest;
import com.finance.common.dto.UserDTO;
import com.finance.common.exception.ExceptionService;
import com.finance.common.service.PasswordEncryptor;

import java.util.function.Function;

import org.springframework.stereotype.Service;

@Service
public class UsernameAuthenticator extends Authenticator {
    private final UserClient userClient;

    protected UsernameAuthenticator(final UserClient userClient, final PasswordEncryptor passwordEncryptor,
            final ExceptionService exceptionService) {
        super(passwordEncryptor, exceptionService);
        this.userClient = userClient;
    }

    @Override
    public Function<AuthenticationRequest, UserDTO> getUserFunction() {
        return request -> userClient.getUserByUsername(request.getUsername());
    }
}
