package com.finance.security.controller;

import com.finance.common.dto.AuthResultDTO;
import com.finance.common.dto.AuthenticationRequest;
import com.finance.security.service.Authenticator;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/authenticate")
@RequiredArgsConstructor
public class AuthenticationController {
    private final Authenticator authenticator;

    @PostMapping
    public AuthResultDTO getAuthentication(@RequestBody AuthenticationRequest authenticationRequest) {
        return authenticator.authenticate(authenticationRequest);
    }

}
