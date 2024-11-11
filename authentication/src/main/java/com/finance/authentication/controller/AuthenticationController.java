package com.finance.authentication.controller;

import com.finance.common.client.auth.dto.AuthenticationDTO;
import com.finance.common.client.auth.dto.WalletUserDTO;

import java.util.ArrayList;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class AuthenticationController {

    @GetMapping("/{username}")
    public AuthenticationDTO getAuthentication(String username) {
        WalletUserDTO walletUserDTO = WalletUserDTO.builder()
            .username(username)
            .build();

        return AuthenticationDTO.builder()
            .walletUser(walletUserDTO)
            .accessUris(new ArrayList<>())
            .build();
    }

}
