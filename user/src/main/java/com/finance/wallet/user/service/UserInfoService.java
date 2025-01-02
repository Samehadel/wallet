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

    public UserDTO getPublicUserByUsername(final String username) {
        requestInfoValidator.validateByUsername(username);
        return getByUsername(username);
    }

    public UserDTO getInternalUserByUsername(final String username) {
        return getByUsername(username);
    }

    private UserDTO getByUsername(final String username) {
        log.info("Getting user by username: {}", username);
        final var userEntity = getUserEntityByUsername(username);

        return mapToUserDTO(userEntity);
    }

    private UserEntity getUserEntityByUsername(final String username) {
        return userRepository.findByUsername(username)
            .orElseThrow(this::getUserNotFoundException);
    }

    public UserDTO getInternalUserByMobile(final String mobile) {
        return getByMobile(mobile);
    }

    private UserDTO getByMobile(final String mobile) {
        log.info("Getting user by mobile: {}", mobile);
        final var userEntity = getUserEntityByMobile(mobile);

        return mapToUserDTO(userEntity);
    }

    private UserEntity getUserEntityByMobile(final String mobile) {
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
