package com.finance.wallet.user.service;

import com.finance.common.dto.UserDTO;
import com.finance.common.exception.BadRequestException;
import com.finance.common.exception.ExceptionService;
import com.finance.common.exception.SharedApplicationError;
import com.finance.common.service.request.RequestInfoValidator;
import com.finance.wallet.user.mapper.UserMapper;
import com.finance.wallet.user.persistence.entity.UserEntity;
import com.finance.wallet.user.persistence.repository.UserRepository;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Service
@RequiredArgsConstructor
@Log4j2
public class UserInfoService {
    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final ExceptionService exceptionService;
    private final RequestInfoValidator requestInfoValidator;

    /**
     * Find the user by username.
     * This method apply request validation because it's intended to be used for public communication.
     *
     * @param username the username to find the user with.
     * @return userDTO for the given username
     */
    public UserDTO findByUsername(final String username) {
        log.info("Getting user by username: {}", username);

        requestInfoValidator.validateByUsername(username);
        final var userEntity = findByUsernameInDB(username);

        return mapToUserDTO(userEntity);
    }

    private UserEntity findByUsernameInDB(final String username) {
        return userRepository.findByUsername(username)
            .orElseThrow(this::getUserNotFoundException);
    }

    public UserDTO findByMobile(final String mobile) {
        log.info("Getting user by mobile: {}", mobile);

        requestInfoValidator.validateByMobile(mobile);
        final var userEntity = findByMobileInDB(mobile);

        return mapToUserDTO(userEntity);
    }

    private UserEntity findByMobileInDB(final String mobile) {
        return userRepository.findByMobile(mobile)
            .orElseThrow(this::getUserNotFoundException);
    }

    private BadRequestException getUserNotFoundException() {
        return exceptionService.buildBadRequestException(SharedApplicationError.USER_NOT_FOUND);
    }

    private UserDTO mapToUserDTO(final UserEntity userEntity) {
        return userMapper.mapToDTO(userEntity);
    }
}
