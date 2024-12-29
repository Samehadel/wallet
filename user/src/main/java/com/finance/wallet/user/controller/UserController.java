package com.finance.wallet.user.controller;

import com.finance.common.constants.RestGroups;
import com.finance.common.dto.UserDTO;
import com.finance.wallet.user.service.UserService;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/public")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/register")
    public UserDTO register(@RequestBody @Validated(RestGroups.Create.class) final UserDTO userDTO) {
        return userService.registerCustomerUser(userDTO);
    }

    @GetMapping("/username/{username}")
    public UserDTO getUserByUsername(@PathVariable final String username) {
        return userService.getPublicUserByUsername(username);
    }
}
