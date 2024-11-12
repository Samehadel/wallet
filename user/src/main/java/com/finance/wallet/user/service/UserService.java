package com.finance.wallet.user.service;

import com.finance.common.constants.UserStatus;
import com.finance.common.dto.AccessUri;
import com.finance.common.dto.AuthenticationDTO;
import com.finance.common.dto.WalletUserDTO;
import com.finance.common.exception.ExceptionService;
import com.finance.wallet.user.exception.UserServiceError;
import com.finance.wallet.user.mapper.UserMapper;
import com.finance.wallet.user.persistence.repository.UserRepository;

import java.util.List;

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

    public WalletUserDTO register(final WalletUserDTO userDTO) {
        log.info("Registering user");

        validateUserNotExists(userDTO);
        final var userEntity = userMapper.mapToEntity(userDTO);

        final var hashedPassword = passwordEncryptor.hashPassword(userDTO.getPassword());
        userEntity.setPassword(hashedPassword);
        userDTO.setPassword(null);

        return userMapper.mapToDTO(userEntity);
    }

    private void validateUserNotExists(final WalletUserDTO userDTO) {
        if (userRepository.existsByUsername(userDTO.getUsername())) {
            exceptionService.throwBadRequestException(UserServiceError.USERNAME_ALREADY_EXISTS);
        }

        if (userRepository.existsByEmail(userDTO.getEmail())) {
            exceptionService.throwBadRequestException(UserServiceError.EMAIL_ALREADY_EXISTS);
        }
    }

    public AuthenticationDTO getAuthenticationByUsername(final java.lang.String username) {
        log.info("Getting user by username: {}", username);

        WalletUserDTO walletUserDTO = WalletUserDTO.builder()
            .username(username)
            .email("sample@example.com")
            .status(UserStatus.ACTIVE)
            .build();

        AccessUri accessUri = AccessUri.builder()
            .uri("/api/v1/user/username/{username}")
            .methodType("GET")
            .build();

        return AuthenticationDTO.builder()
            .walletUser(walletUserDTO)
            .accessUris(List.of(accessUri))
            .build();
    }
}
