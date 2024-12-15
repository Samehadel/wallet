package com.finance.common.util;

import lombok.NoArgsConstructor;

@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class ObjectUtils {

    public static boolean anyNull(final Object... objects) {
        for (final Object object : objects) {
            if (object == null) {
                return true;
            }
        }
        return false;
    }

    public static boolean isNull(final Object object) {
        return object == null;
    }

    public static boolean notNull(final Object object) {
        return object != null;
    }
}
