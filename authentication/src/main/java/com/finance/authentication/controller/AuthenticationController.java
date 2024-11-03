package com.finance.authentication.controller;

import com.finance.authentication.client.UserServiceClient;
import com.finance.common.dto.UserDTO;
import com.finance.common.model.ApiResponse;
import com.finance.common.util.ApiResponseBuilder;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class AuthenticationController {
    private final UserServiceClient userServiceClient;

    @GetMapping("/test")
    public ApiResponse<UserDTO> test() {
        UserDTO userDTO = userServiceClient.getUserByUsername("test");

        return ApiResponseBuilder.buildSuccessResponse(userDTO);
    }

}
