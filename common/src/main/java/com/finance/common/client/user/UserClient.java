package com.finance.common.client.user;

import com.finance.common.client.user.dto.UserDTO;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "user-svc")
public interface UserClient {
    String USER_API = "/api/v1/user";

    @GetMapping(USER_API + "/username/{username}")
    UserDTO getUserByUsername(@PathVariable("username") String username);
}
