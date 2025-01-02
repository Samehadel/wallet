package com.finance.wallet.user.service;

import com.finance.common.constants.DatabaseConfigKeys;
import com.finance.common.constants.UserStatusEnum;
import com.finance.common.exception.ExceptionService;
import com.finance.common.exception.SharedApplicationError;
import com.finance.common.service.ServiceConfiguration;
import com.finance.common.util.StringUtil;
import com.finance.wallet.user.persistence.entity.UserEntity;
import com.finance.wallet.user.persistence.repository.UserRepository;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Service
@RequiredArgsConstructor
@Log4j2
public class UserStateService {
    private final UserRepository userRepository;
    private final ExceptionService exceptionService;
    private final ServiceConfiguration serviceConfiguration;

    public void updateLastLoginDate(final Long userId) {
        log.info("Updating last login date for user with id: {}", userId);
        final var userEntity = getUserById(userId);

        userEntity.setLastLoginDate(LocalDateTime.now());
        userRepository.save(userEntity);
    }

    public void incrementFailedLoginTrials(final Long userId) {
        log.info("Incrementing login trials for user with id: {}", userId);
        final var userEntity = getUserById(userId);

        final var loginTrials = userEntity.getLoginTrials() + 1;
        userEntity.setLoginTrials(loginTrials);
        lockUserIfMaxTrialsReached(userEntity);
        userRepository.save(userEntity);
    }

    private void lockUserIfMaxTrialsReached(final UserEntity userEntity) {
        if (userEntity.getLoginTrials() >= getMaxLoginTrials()) {
            userEntity.setStatus(UserStatusEnum.LOCKED);
            userEntity.setStatusUpdatedAt(LocalDateTime.now());
        }
    }

    private Integer getMaxLoginTrials() {
        final String maxLoginTrailsString = serviceConfiguration.getConfiguration(DatabaseConfigKeys.MAX_LOGIN_TRIALS);

        if (StringUtil.isNullOrEmpty(maxLoginTrailsString)) {
            throw exceptionService.buildInternalExceptionWithReference(SharedApplicationError.CONFIGURATION_ERROR);
        }

        return Integer.parseInt(maxLoginTrailsString);
    }

    private UserEntity getUserById(final Long userId) {
        return userRepository.findById(userId)
            .orElseThrow(() -> exceptionService.buildBadRequestException(SharedApplicationError.USER_NOT_FOUND));
    }
}
