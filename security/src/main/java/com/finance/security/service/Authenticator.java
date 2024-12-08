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
import com.finance.common.util.CollectionUtil;
import com.finance.common.util.StringUtil;
import com.finance.security.event.UserEventProducer;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Service
@RequiredArgsConstructor
@Log4j2
public class Authenticator {
    private final PasswordEncryptor passwordEncryptor;
    private final ExceptionService exceptionService;
    private final UserClient userClient;
    private final UserEventProducer userEventProducer;

    public AuthResultDTO authenticate(final AuthenticationRequest authenticationRequest) {
        final UserDTO user = fetchUser(authenticationRequest);

        if (user == null) {
            throw exceptionService.buildBadRequestException(SharedApplicationError.USER_NOT_FOUND);
        }

        if (user.getStatus() == null) {
            throw exceptionService.buildBadRequestException(SharedApplicationError.GENERIC_ERROR);
        }

        final boolean passwordMatch = passwordMatch(authenticationRequest.getPassword(), user.getPassword());
        if (!passwordMatch) {
            incrementFailedLoginAttempts(user.getId());
            return createAuthResult(AuthResultEnum.INVALID_CREDENTIALS);
        }

        if (!UserStatusEnum.statusAllowedForLogin(user.getStatus())) {
            incrementFailedLoginAttempts(user.getId());
            return createAuthResult(AuthResultEnum.INVALID_USER_STATUS);
        }

        updateLastLoginDate(user.getId());
        return createAuthResult(AuthResultEnum.AUTHENTICATED);
    }

    private boolean passwordMatch(final char[] userHashedPassword, final char[] authenticationPassword) {
        if (CollectionUtil.arrayNullOrEmpty(userHashedPassword)
            || CollectionUtil.arrayNullOrEmpty(authenticationPassword)) {
            return false;
        }
        return passwordEncryptor.passwordMatch(userHashedPassword, String.valueOf(authenticationPassword));
    }

    private UserDTO fetchUser(final AuthenticationRequest authenticationRequest) {
        if (!StringUtil.isNullOrEmpty(authenticationRequest.getMobile())) {
            return userClient.getUserByMobile(authenticationRequest.getMobile());
        } else if (!StringUtil.isNullOrEmpty(authenticationRequest.getUsername())) {
            return userClient.getUserByUsername(authenticationRequest.getUsername());
        }

        throw exceptionService.buildBadExceptionWithReference(SharedApplicationError.GENERIC_ERROR);
    }

    private void incrementFailedLoginAttempts(final Long userId) {
        userEventProducer.pushIncreaseFailedLoginAttemptsEvent(userId);
    }

    private AuthResultDTO createAuthResult(final AuthResultEnum authenticated) {
        return AuthResultDTO.builder()
            .authResult(authenticated)
            .build();
    }

    private void updateLastLoginDate(final Long userId) {
        userEventProducer.pushUserLoginEvent(userId);
    }
}
