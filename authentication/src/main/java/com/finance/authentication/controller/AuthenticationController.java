package com.finance.authentication.controller;

import com.finance.common.constants.UrlMethodEnum;
import com.finance.common.constants.UserStatusEnum;
import com.finance.common.dto.AccessUriDTO;
import com.finance.common.dto.AuthenticationDTO;
import com.finance.common.dto.UserDTO;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class AuthenticationController {

    @GetMapping("/{username}")
    public AuthenticationDTO getAuthentication(@PathVariable("username") String username) {
        UserDTO walletUserDTO = UserDTO.builder()
            .username(username)
            .email("sample@example.com")
            .status(UserStatusEnum.ACTIVE)
            .build();

        AccessUriDTO accessUri = AccessUriDTO.builder()
            .url("/api/v1/user/username/{username}")
            .method(UrlMethodEnum.GET)
            .build();

        return AuthenticationDTO.builder()
            .walletUser(walletUserDTO)
            .accessUris(List.of(accessUri))
            .build();
    }

}
