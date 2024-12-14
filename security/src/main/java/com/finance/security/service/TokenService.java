package com.finance.security.service;

public interface TokenService {
    String generateToken(String username);

    String parseForUsername(String jwt);
}
