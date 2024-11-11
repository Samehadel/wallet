package com.finance.common.constants;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;


@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class CommonHeaders {

    public static final String X_USER = "X-User";
    public static final String X_TOKEN = "X-Token";
    public static final String X_CHANNEL_ID = "X-Channel-Id";
}
