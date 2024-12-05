package com.finance.wallet.user.service;

import com.finance.common.constants.UrlMethodEnum;
import com.finance.common.constants.UserStatusEnum;
import com.finance.common.dto.AccessUriDTO;
import com.finance.common.dto.UserDTO;
import com.finance.common.exception.ExceptionService;
import com.finance.wallet.user.exception.UserServiceError;
import com.finance.wallet.user.mapper.UserMapper;
import com.finance.wallet.user.persistence.repository.UserRepository;

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
            .orElseThrow(() -> exceptionService.throwBadRequestException(UserServiceError.USER_NOT_FOUND));


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
}
