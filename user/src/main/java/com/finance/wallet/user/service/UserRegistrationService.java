package com.finance.wallet.user.service;

import com.finance.common.dto.UserDTO;
import com.finance.common.exception.ExceptionService;
import com.finance.common.service.PasswordEncryptor;
import com.finance.wallet.user.exception.UserServiceError;
import com.finance.wallet.user.mapper.UserMapper;
import com.finance.wallet.user.persistence.repository.UserRepository;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Service
@RequiredArgsConstructor
@Log4j2
public class UserRegistrationService {
    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final ExceptionService exceptionService;
    private final PasswordEncryptor passwordEncryptor;

    public UserDTO registerCustomerUser(final UserDTO userDTO) {
        log.info("Registering customer user");
        validateUserNotExists(userDTO);
        var userEntity = userMapper.mapToEntity(userDTO);
        final var hashedPassword = passwordEncryptor.hashPassword(userDTO.getPassword());
        userEntity.setPassword(hashedPassword);
        userDTO.setPassword(null);

        userEntity = userRepository.save(userEntity);
        log.info("User registered successfully");

        return userMapper.mapToDTO(userEntity, true);
    }

    private void validateUserNotExists(final UserDTO userDTO) {
        if (userRepository.existsByUsername(userDTO.getUsername())) {
            throw exceptionService.buildBadRequestException(UserServiceError.USERNAME_ALREADY_EXISTS);
        }

        if (userRepository.existsByMobile(userDTO.getMobile())) {
            throw exceptionService.buildBadRequestException(UserServiceError.MOBILE_ALREADY_EXISTS);
        }
    }
}
