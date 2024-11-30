package com.finance.common.constants;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserStatus {
    ACTIVE("ACTIVE"),
    INACTIVE("INACTIVE"),
    BLOCKED("BLOCKED"),
    LOCKED("LOCKED");

    private final String status;

    public static boolean isActive(final UserStatus status) {
        return status == ACTIVE;
    }

    public static boolean isInactive(final UserStatus status) {
        return status == INACTIVE;
    }
}
