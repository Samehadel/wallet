package com.finance.wallet.user.controller;

import com.finance.common.client.payload.dto.UserDTO;
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

    @GetMapping("username/{username}")
    public UserDTO getUserByUsername(@PathVariable("username") final String username) {
        return userService.getUserByUsername(username);
    }
}
