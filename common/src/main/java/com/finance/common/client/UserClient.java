package com.finance.common.client;


import com.finance.common.dto.UserDTO;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "user-svc")
public interface UserClient {
    java.lang.String USER_API = "/api/v1/user";

    @GetMapping(USER_API + "/username/{username}")
    UserDTO getUserByUsername(@PathVariable("username") java.lang.String username);

    @GetMapping(USER_API + "/mobile/{mobile}")
    UserDTO getUserByMobile(@PathVariable("mobile") String mobile);
}
