package com.finance.security.controller;

import com.finance.common.dto.AuthResultDTO;
import com.finance.common.dto.AuthorizationRequest;
import com.finance.security.service.AuthorizationManager;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/internal/authorization")
@RequiredArgsConstructor
public class AuthorizationController {
    private final AuthorizationManager authorizationManager;

    @PostMapping
    public AuthResultDTO authorize(final AuthorizationRequest authRequest) {
        return authorizationManager.authorize(authRequest);
    }
}
