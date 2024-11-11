package com.finance.wallet.user.service;

import com.finance.common.constants.UserStatus;
import com.finance.common.client.payload.dto.UserDTO;
import com.finance.common.exception.ExceptionService;
import com.finance.wallet.user.exception.UserServiceError;
import com.finance.wallet.user.mapper.UserMapper;
import com.finance.wallet.user.persistence.repository.UserRepository;

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
            exceptionService.throwBadRequestException(UserServiceError.USERNAME_ALREADY_EXISTS);
        }

        if (userRepository.existsByEmail(userDTO.getEmail())) {
            exceptionService.throwBadRequestException(UserServiceError.EMAIL_ALREADY_EXISTS);
        }
    }

    public UserDTO getUserByUsername(final String username) {
        log.info("Getting user by username: {}", username);

        UserDTO sampleUser = new UserDTO();
        sampleUser.setUsername(username);;
        sampleUser.setEmail("sample@mail.com");
        sampleUser.setStatus(UserStatus.ACTIVE);
        sampleUser.setLoginTries(0);
        sampleUser.setLocked(false);

        return sampleUser;
    }
}
