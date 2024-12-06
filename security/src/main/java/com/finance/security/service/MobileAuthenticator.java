package com.finance.security.service;

import com.finance.common.client.UserClient;
import com.finance.common.constants.AuthResultEnum;
import com.finance.common.constants.UserStatusEnum;
import com.finance.common.dto.AuthResultDTO;
import com.finance.common.dto.AuthenticationRequest;
import com.finance.common.dto.UserDTO;
import com.finance.common.exception.ExceptionService;
import com.finance.common.exception.SharedApplicationError;
import com.finance.common.service.PasswordEncryptor;


import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class MobileAuthenticator implements Authenticator {
    private final UserClient userClient;
    private final PasswordEncryptor passwordEncryptor;
    private final ExceptionService exceptionService;

    @Override
    public AuthResultDTO authenticate(AuthenticationRequest authenticationRequest) {
        UserDTO user = userClient.getUserByMobile(authenticationRequest.getMobile());

        if (user == null) {
            throw exceptionService.throwBadRequestException(SharedApplicationError.USER_NOT_FOUND);
        }
        boolean passwordMatch = !passwordEncryptor.checkPassword(authenticationRequest.getPassword(), String.valueOf(user.getPassword()));

        if (!passwordMatch) {
            return AuthResultDTO.builder()
                .authResult(AuthResultEnum.INVALID_CREDENTIALS)
                .build();
        }

        if (user.getStatus() == null) {
            throw exceptionService.throwBadRequestException(SharedApplicationError.GENERIC_ERROR);
        }

        if (UserStatusEnum.statusAllowedForLogin(user.getStatus())) {
            return AuthResultDTO.builder()
                .authResult(AuthResultEnum.NOT_AUTHENTICATED)
                .build();
        }

        return AuthResultDTO.builder()
            .authResult(AuthResultEnum.AUTHENTICATED)
            .build();
    }
}
