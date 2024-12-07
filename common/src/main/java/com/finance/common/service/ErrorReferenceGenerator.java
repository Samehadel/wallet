package com.finance.common.service;

import java.util.Random;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ErrorReferenceGenerator {
    private static final Random RANDOM = new Random();

    public static String generateErrorReverence() {
        return String.valueOf(RANDOM.nextInt(9000) + 1000);
    }
}
