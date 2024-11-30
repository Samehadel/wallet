package com.finance.wallet.user.service;

import com.finance.common.constants.UserStatus;
import com.finance.common.dto.AccessUri;
import com.finance.common.dto.AuthenticationDTO;
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

    public UserDTO register(final UserDTO userDTO) {
        log.info("Registering user");

        validateUserNotExists(userDTO);
        final var userEntity = userMapper.mapToEntity(userDTO);

        final var hashedPassword = passwordEncryptor.hashPassword(userDTO.getPassword());
        userEntity.setPassword(hashedPassword);
        userDTO.setPassword(null);

        return userMapper.mapToDTO(userEntity);
    }

    private void validateUserNotExists(final UserDTO userDTO) {
        if (userRepository.existsByUsername(userDTO.getUsername())) {
            throw exceptionService.throwBadRequestException(UserServiceError.USERNAME_ALREADY_EXISTS);
        }

        if (userRepository.existsByEmail(userDTO.getEmail())) {
            throw exceptionService.throwBadRequestException(UserServiceError.EMAIL_ALREADY_EXISTS);
        }
    }

    public AuthenticationDTO getAuthenticationByUsername(final java.lang.String username) {
        log.info("Getting user by username: {}", username);

        if (mockUserEnabled) {
            return getMockUser(username);
        }

        exceptionService.throwInternalException(UserServiceError.USER_NOT_FOUND, username);
        return null;
    }

    private AuthenticationDTO getMockUser(final String username) {
        UserDTO userDTO = UserDTO.builder()
            .username(username)
            .email("sample@example.com")
            .status(UserStatus.ACTIVE)
            .build();

        AccessUri accessUri = AccessUri.builder()
            .uri("user/username/{username}")
            .methodType("GET")
            .build();

        return AuthenticationDTO.builder()
            .walletUser(userDTO)
            .accessUris(List.of(accessUri))
            .build();
    }
}
