package com.finance.security.service;

import com.finance.common.exception.ExceptionService;
import com.finance.common.util.StringUtil;
import com.finance.security.exception.SecurityServiceError;

import java.util.Collections;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import javax.crypto.SecretKey;

@Service
@RequiredArgsConstructor
@Log4j2
public class JwtService implements TokenService {
    private final ExceptionService exceptionService;

    @Value("${user.token.secret:secret}")
    private String tokenSecret;


    @Value("${user.token.expiration.sec:3600}")
    private Integer jwtExpirationSec;

    @Override
    public String generateToken(final String username) {
        return Jwts.builder()
            .subject(username)
            .issuedAt(new Date())
            .expiration(getExpirationTime())
            .signWith(getSigningKey())
            .claims(Collections.emptyMap())
            .compact();
    }

    private Date getExpirationTime() {
        return new Date(System.currentTimeMillis() + jwtExpirationSec * 1000);
    }

    private SecretKey getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(tokenSecret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    @Override
    public String parseForUsername(String token) {
        try {
            if (StringUtil.isNullOrEmpty(token)) {
                return null;
            }
            Claims payload = getJWTClaims(token);
            return payload.getSubject();
        } catch (SignatureException exception) {
            log.error("Failed to parse token due to [{}]", exception.getMessage());
            throw exceptionService.buildUnauthorizedException(SecurityServiceError.INVALID_TOKEN);
        }
    }

    private Claims getJWTClaims(String jwt) throws JwtException {
        try {
            return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(jwt)
                .getPayload();
        } catch (Exception exception) {
            log.error("Failed to parse token due to [{}]", exception.getMessage());
            throw exceptionService.buildUnauthorizedException(SecurityServiceError.TOKEN_EXPIRED);
        }
    }
}
