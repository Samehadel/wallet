package com.finance.wallet.user.controller;

import com.finance.common.dto.UserDTO;
import com.finance.common.model.ApiResponse;
import com.finance.common.util.ApiResponseBuilder;
import com.finance.wallet.user.service.UserService;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/register")
    public ApiResponse<UserDTO> register(@RequestBody final UserDTO userDTO) {
        return ApiResponseBuilder.buildSuccessResponse(userService.register(userDTO));
    }
}
