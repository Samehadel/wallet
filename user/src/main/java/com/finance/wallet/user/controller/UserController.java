package com.finance.wallet.user.controller;

import com.finance.common.dto.AuthenticationDTO;
import com.finance.common.dto.UserDTO;
import com.finance.wallet.user.service.UserService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/register")
    public UserDTO register(@RequestBody final UserDTO userDTO) {
        return userService.register(userDTO);
    }

    @GetMapping("/username/{username}")
    public AuthenticationDTO getAuthentication(@PathVariable("username") final java.lang.String username) {
        return userService.getAuthenticationByUsername(username);
    }
}
