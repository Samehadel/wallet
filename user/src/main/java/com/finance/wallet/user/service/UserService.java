package com.finance.wallet.user.service;

import com.finance.common.constants.DatabaseConfigKeys;
import com.finance.common.constants.UrlMethodEnum;
import com.finance.common.constants.UserStatusEnum;
import com.finance.common.dto.AccessUriDTO;
import com.finance.common.dto.UserDTO;
import com.finance.common.exception.ExceptionService;
import com.finance.common.exception.SharedApplicationError;
import com.finance.common.service.PasswordEncryptor;
import com.finance.common.service.ServiceConfiguration;
import com.finance.common.util.StringUtil;
import com.finance.wallet.user.exception.UserServiceError;
import com.finance.wallet.user.mapper.UserMapper;
import com.finance.wallet.user.persistence.entity.UserEntity;
import com.finance.wallet.user.persistence.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Service
@RequiredArgsConstructor
@Log4j2
public class UserService {
    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final ExceptionService exceptionService;
    private final PasswordEncryptor passwordEncryptor;
    private final ServiceConfiguration serviceConfiguration;

    @Value("${user.mock.enable:false}")
    private boolean mockUserEnabled;

    public UserDTO registerCustomerUser(final UserDTO userDTO) {
        log.info("Registering user");
        validateUserNotExists(userDTO);
        var userEntity = userMapper.mapToEntity(userDTO);
        final var hashedPassword = passwordEncryptor.hashPassword(userDTO.getPassword());
        userEntity.setPassword(hashedPassword);
        userDTO.setPassword(null);

        userEntity = userRepository.save(userEntity);
        return userMapper.mapToDTO(userEntity);
    }

    private void validateUserNotExists(final UserDTO userDTO) {
        if (userRepository.existsByUsername(userDTO.getUsername())) {
            throw exceptionService.throwBadRequestException(UserServiceError.USERNAME_ALREADY_EXISTS);
        }

        if (userRepository.existsByMobile(userDTO.getMobile())) {
            throw exceptionService.throwBadRequestException(UserServiceError.MOBILE_ALREADY_EXISTS);
        }
    }

    public UserDTO getByUsername(final String username) {
        log.info("Getting user by username: {}", username);

        if (mockUserEnabled) {
            return getMockUser(username);
        }

        final var userEntity = userRepository.findByUsername(username)
            .orElseThrow(() -> exceptionService.throwBadRequestException(SharedApplicationError.USER_NOT_FOUND));

        return userMapper.mapToDTO(userEntity);
    }

    public UserDTO getByMobile(final String mobile) {
        log.info("Getting user by mobile: {}", mobile);

        if (mockUserEnabled) {
            return getMockUser("mockUser " + mobile);
        }

        final var userEntity = userRepository.findByMobile(mobile)
            .orElseThrow(() -> exceptionService.throwBadRequestException(SharedApplicationError.USER_NOT_FOUND));

        return userMapper.mapToDTO(userEntity);
    }
    private UserDTO getMockUser(final String username) {
        AccessUriDTO accessUri = AccessUriDTO.builder()
            .url("user/username/{username}")
            .method(UrlMethodEnum.GET)
            .build();

        return UserDTO.builder()
            .username(username)
            .email("sample@example.com")
            .status(UserStatusEnum.ACTIVE)
            .accessUris(List.of(accessUri))
            .build();
    }

    private UserDTO block(final Long userId) {
        log.info("Blocking user with id: {}", userId);
        final var userEntity = userRepository.findById(userId)
            .orElseThrow(() -> exceptionService.throwBadRequestException(SharedApplicationError.USER_NOT_FOUND));

        userEntity.setStatus(UserStatusEnum.BLOCKED);
        userEntity.setStatusUpdatedAt(LocalDateTime.now());

        final var blockedUser = userRepository.save(userEntity);
        return userMapper.mapToDTO(blockedUser);
    }

    public void updateLastLoginDate(final Long userId) {
        log.info("Updating last login date for user with id: {}", userId);
        final var userEntity = userRepository.findById(userId)
            .orElseThrow(() -> exceptionService.throwBadRequestException(SharedApplicationError.USER_NOT_FOUND));

        userEntity.setLastLoginDate(LocalDateTime.now());
        userRepository.save(userEntity);
    }

    public void resetLoginTrials(final Long userId) {
        log.info("Resetting login trials for user with id: {}", userId);
        final var userEntity = userRepository.findById(userId)
            .orElseThrow(() -> exceptionService.throwBadRequestException(SharedApplicationError.USER_NOT_FOUND));

        userEntity.setLoginTrials(0);
        userRepository.save(userEntity);
    }

    public int incrementFailedLoginTrials(final Long userId) {
        log.info("Incrementing login trials for user with id: {}", userId);
        final var userEntity = userRepository.findById(userId)
            .orElseThrow(() -> exceptionService.throwBadRequestException(SharedApplicationError.USER_NOT_FOUND));

        final var loginTrials = userEntity.getLoginTrials() + 1;
        userEntity.setLoginTrials(loginTrials);
        lockUserIfMaxTrialsReached(userEntity);
        userRepository.save(userEntity);

        return loginTrials;
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
            throw exceptionService.throwInternalException(SharedApplicationError.CONFIGURATION_ERROR, "MLT-1");
        }

        return Integer.parseInt(maxLoginTrailsString);
    }

    public void unlockUser(final Long userId) {
        log.info("Unlocking user with id: {}", userId);
        final var userEntity = userRepository.findById(userId)
            .orElseThrow(() -> exceptionService.throwBadRequestException(SharedApplicationError.USER_NOT_FOUND));

        userEntity.setStatus(UserStatusEnum.ACTIVE);
        userEntity.setStatusUpdatedAt(LocalDateTime.now());
        userRepository.save(userEntity);
    }
}
