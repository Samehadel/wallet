package com.finance.wallet.user.controller;

import com.finance.common.constants.RestGroups;
import com.finance.common.dto.UserDTO;
import com.finance.wallet.user.service.UserInfoService;
import com.finance.wallet.user.service.UserRegistrationService;

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
    private final UserInfoService userInfoService;
    private final UserRegistrationService userRegistrationService;

    @PostMapping("/register")
    public UserDTO register(@RequestBody @Validated(RestGroups.Create.class) final UserDTO userDTO) {
        return userRegistrationService.registerCustomerUser(userDTO);
    }

    @GetMapping("/username/{username}")
    public UserDTO getUserByUsername(@PathVariable final String username) {
        return userInfoService.findByUsernameExcludeSensitiveInfo(username);
    }
}
