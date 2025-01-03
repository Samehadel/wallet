package com.finance.common.constants;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum RequestSource {
    INTERNAL("INTERNAL"),
    EXTERNAL("EXTERNAL");

    private final String value;
}
