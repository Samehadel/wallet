package com.finance.common.client.auth;

import com.finance.common.client.auth.dto.AuthenticationDTO;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import reactor.core.publisher.Mono;

@FeignClient(name = "auth-svc")
public interface AuthenticationClient {
    String AUTH_API = "/api/v1/auth";

    @GetMapping(AUTH_API + "/{username}")
    Mono<AuthenticationDTO> getAuthentication(@PathVariable("username") String username);
}
