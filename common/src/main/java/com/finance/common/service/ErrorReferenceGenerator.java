package com.finance.common.service;

import java.util.Random;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ErrorReferenceGenerator {
    private static final Random RANDOM = new Random();

    public static String generateErrorReverence(final String service) {
        return String.format("%s-%d", service, RANDOM.nextInt(9000) + 1000);
    }
}
