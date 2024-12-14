package com.finance.wallet.user.controller;

import com.finance.common.dto.UserDTO;
import com.finance.wallet.user.service.UserService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/internal")
@RequiredArgsConstructor
public class InternalUserController {
    private final UserService userService;

    @GetMapping("/username/{username}")
    public UserDTO getUserByUsername(@PathVariable("username") final String username) {
        return userService.getByUsername(username);
    }

    @GetMapping("/mobile/{mobile}")
    public UserDTO getUserByMobile(@PathVariable("mobile") final String mobile) {
        return userService.getByMobile(mobile);
    }
}