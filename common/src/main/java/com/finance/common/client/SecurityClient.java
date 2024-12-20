package com.finance.common.client;


import com.finance.common.dto.AuthResultDTO;
import com.finance.common.dto.AuthorizationRequest;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "security-svc")
public interface SecurityClient {
    String SERVICE_BASE_URL = "/api/v1/security/internal";
    String AUTHORIZATION_URL = SERVICE_BASE_URL + "/authorization";

    @PostMapping(AUTHORIZATION_URL)
    AuthResultDTO authorize(AuthorizationRequest authRequest);
}
