package com.finance.security.service;

import com.finance.common.constants.AuthResultEnum;
import com.finance.common.constants.UserStatusEnum;
import com.finance.common.dto.AuthResultDTO;
import com.finance.common.dto.AuthenticationRequest;
import com.finance.common.dto.UserDTO;
import com.finance.common.exception.ExceptionService;
import com.finance.common.exception.SharedApplicationError;
import com.finance.common.service.PasswordEncryptor;

import java.util.function.Function;

public abstract class Authenticator {
    protected final PasswordEncryptor passwordEncryptor;
    protected final ExceptionService exceptionService;

    Authenticator(PasswordEncryptor passwordEncryptor, ExceptionService exceptionService) {
        this.passwordEncryptor = passwordEncryptor;
        this.exceptionService = exceptionService;
    }

    public AuthResultDTO authenticate(final AuthenticationRequest authenticationRequest) {
        final UserDTO user = getUserFunction().apply(authenticationRequest);

        if (user == null) {
            throw exceptionService.throwBadRequestException(SharedApplicationError.USER_NOT_FOUND);
        }

        final boolean passwordMatch = !passwordEncryptor.passwordMatch(authenticationRequest.getPassword(), String.valueOf(user.getPassword()));
        if (!passwordMatch) {
            return createAuthResult(AuthResultEnum.INVALID_CREDENTIALS);
        }

        if (user.getStatus() == null) {
            throw exceptionService.throwBadRequestException(SharedApplicationError.GENERIC_ERROR);
        }

        if (UserStatusEnum.statusAllowedForLogin(user.getStatus())) {
            return createAuthResult(AuthResultEnum.NOT_AUTHENTICATED);
        }

        return createAuthResult(AuthResultEnum.AUTHENTICATED);
    }

    private AuthResultDTO createAuthResult(final AuthResultEnum authenticated) {
        return AuthResultDTO.builder()
            .authResult(authenticated)
            .build();
    }

    abstract Function<AuthenticationRequest, UserDTO> getUserFunction();
}
