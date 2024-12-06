package com.finance.common.constants;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserStatusEnum {
    ACTIVE("ACTIVE"),
    INACTIVE("INACTIVE"),
    BLOCKED("BLOCKED"),
    LOCKED("LOCKED");

    private final String status;

    public static boolean isActive(final UserStatusEnum status) {
        return status == ACTIVE;
    }

    public static boolean isInactive(final UserStatusEnum status) {
        return status == INACTIVE;
    }

    public static boolean statusAllowedForLogin(final UserStatusEnum status) {
        if (status == null) {
            return false;
        }

        return status == ACTIVE;
    }
}
