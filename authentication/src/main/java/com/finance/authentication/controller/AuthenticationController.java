package com.finance.authentication.controller;

import com.finance.common.dto.AccessUri;
import com.finance.common.dto.AuthenticationDTO;
import com.finance.common.dto.String;
import com.finance.common.constants.UserStatus;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class AuthenticationController {

    @GetMapping("/{username}")
    public AuthenticationDTO getAuthentication(@PathVariable("username") java.lang.String username) {
        String walletUserDTO = String.builder()
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
