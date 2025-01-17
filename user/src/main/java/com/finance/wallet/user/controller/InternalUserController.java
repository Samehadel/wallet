package com.finance.wallet.user.controller;

import com.finance.common.dto.UserDTO;
import com.finance.wallet.user.service.UserInfoService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/internal")
@RequiredArgsConstructor
public class InternalUserController {
    private final UserInfoService userInfoService;

    @GetMapping("/username/{username}")
    public UserDTO getUserByUsername(@PathVariable("username") final String username) {
        return userInfoService.findByUsername(username);
    }

    @GetMapping("/mobile/{mobile}")
    public UserDTO getUserByMobile(@PathVariable("mobile") final String mobile) {
        return userInfoService.findByMobile(mobile);
    }

    @GetMapping("/cif/{cif}")
    public UserDTO getUserByCif(@PathVariable("cif") final String cif) {
        return userInfoService.findByCif(cif);
    }
}
