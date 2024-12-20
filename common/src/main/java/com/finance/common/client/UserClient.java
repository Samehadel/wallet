package com.finance.common.client;


import com.finance.common.dto.AccessUrlDTO;
import com.finance.common.dto.UserDTO;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "user-svc")
public interface UserClient {
    String SERVICE_BASE_URL = "/api/v1/user/internal";
    java.lang.String USER_API = SERVICE_BASE_URL;
    java.lang.String ENDPOINT_API = SERVICE_BASE_URL + "/endpoint";

    @GetMapping(USER_API + "/username/{username}")
    UserDTO getUserByUsername(@PathVariable("username") java.lang.String username);

    @GetMapping(USER_API + "/mobile/{mobile}")
    UserDTO getUserByMobile(@PathVariable("mobile") String mobile);

    @GetMapping(ENDPOINT_API)
    List<AccessUrlDTO> getPublicEndpoints();

    @GetMapping(ENDPOINT_API + "/username/{username}")
    List<AccessUrlDTO> getUserEndpoints(@PathVariable("username") final String username);
}
