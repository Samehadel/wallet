package com.finance.authentication.client;

import com.finance.common.dto.UserDTO;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "k8s001-uat-user-svc")
public interface UserServiceClient {

    @GetMapping("/user/username/{username}")
    UserDTO getUserByUsername(@PathVariable("username") String username);
}