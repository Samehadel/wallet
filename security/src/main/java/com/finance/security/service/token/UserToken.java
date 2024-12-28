package com.finance.security.service.token;

import com.finance.common.dto.Cacheable;

import java.time.LocalDateTime;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor
@Getter
@Builder(access = AccessLevel.PROTECTED)
public class UserToken implements Cacheable {
    private String token;

    private LocalDateTime lastAccessTime;

    private LocalDateTime expirationTime;

    private Long userId;

    private String cif;

    private String username;

    void setLastAccessTime(final LocalDateTime lastAccessTime) {
        this.lastAccessTime = lastAccessTime;
    }
}
