package com.finance.authentication.controller;

import com.finance.common.client.UserClient;
import com.finance.common.client.payload.dto.UserDTO;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class AuthenticationController {
    private final UserClient userClient;

    @GetMapping("/test")
    public UserDTO test() {
        return userClient.getUserByUsername("test");
    }

}
