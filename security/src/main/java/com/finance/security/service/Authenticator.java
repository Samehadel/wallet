package com.finance.security.service;

import com.finance.common.dto.AuthResultDTO;
import com.finance.common.dto.AuthenticationRequest;

public interface Authenticator {
    AuthResultDTO authenticate(AuthenticationRequest authenticationRequest);
}
